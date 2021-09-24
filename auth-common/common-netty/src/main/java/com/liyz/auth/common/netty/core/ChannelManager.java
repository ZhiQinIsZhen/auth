package com.liyz.auth.common.netty.core;

import com.liyz.auth.common.netty.constant.NettyServerConstant;
import com.liyz.auth.common.netty.util.ChannelUtil;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/24 10:17
 */
@Slf4j
public class ChannelManager {

    private static final ConcurrentMap<Channel, RpcContext> IDENTIFIED_CHANNELS = new ConcurrentHashMap<>();

    /**
     * resourceId -> applicationId -> ip -> port -> RpcContext
     */
    private static final ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String,
            ConcurrentMap<Integer, RpcContext>>>> RM_CHANNELS = new ConcurrentHashMap<>();

    /**
     * ip+appname,port
     */
    private static final ConcurrentMap<String, ConcurrentMap<Integer, RpcContext>> TM_CHANNELS
            = new ConcurrentHashMap<>();

    /**
     * Is registered boolean.
     *
     * @param channel the channel
     * @return the boolean
     */
    public static boolean isRegistered(Channel channel) {
        return IDENTIFIED_CHANNELS.containsKey(channel);
    }

    /**
     * Gets get context from identified.
     *
     * @param channel the channel
     * @return the get context from identified
     */
    public static RpcContext getContextFromIdentified(Channel channel) {
        return IDENTIFIED_CHANNELS.get(channel);
    }

    private static String buildClientId(String applicationId, Channel channel) {
        return applicationId + NettyServerConstant.CLIENT_ID_SPLIT_CHAR + ChannelUtil.getAddressFromChannel(channel);
    }

    private static String[] readClientId(String clientId) {
        return clientId.split(NettyServerConstant.CLIENT_ID_SPLIT_CHAR);
    }

    private static RpcContext buildChannelHolder(String version, String applicationId,
                                                 String txServiceGroup, String dbkeys, Channel channel) {
        RpcContext holder = new RpcContext();
        holder.setVersion(version);
        holder.setClientId(buildClientId(applicationId, channel));
        holder.setApplicationId(applicationId);
        holder.setTransactionServiceGroup(txServiceGroup);
        holder.addResources(dbKeytoSet(dbkeys));
        holder.setChannel(channel);
        return holder;
    }

    private static void updateChannelsResource(String resourceId, String clientIp, String applicationId) {
        ConcurrentMap<Integer, RpcContext> sourcePortMap = RM_CHANNELS.get(resourceId).get(applicationId).get(clientIp);
        for (ConcurrentMap.Entry<String, ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Integer,
                RpcContext>>>> rmChannelEntry : RM_CHANNELS.entrySet()) {
            if (rmChannelEntry.getKey().equals(resourceId)) { continue; }
            ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Integer,
                    RpcContext>>> applicationIdMap = rmChannelEntry.getValue();
            if (!applicationIdMap.containsKey(applicationId)) { continue; }
            ConcurrentMap<String, ConcurrentMap<Integer,
                    RpcContext>> clientIpMap = applicationIdMap.get(applicationId);
            if (!clientIpMap.containsKey(clientIp)) { continue; }
            ConcurrentMap<Integer, RpcContext> portMap = clientIpMap.get(clientIp);
            for (ConcurrentMap.Entry<Integer, RpcContext> portMapEntry : portMap.entrySet()) {
                Integer port = portMapEntry.getKey();
                if (!sourcePortMap.containsKey(port)) {
                    RpcContext rpcContext = portMapEntry.getValue();
                    sourcePortMap.put(port, rpcContext);
                    rpcContext.holdInResourceManagerChannels(resourceId, port);
                }
            }
        }
    }

    private static Set<String> dbKeytoSet(String dbkey) {
        if (StringUtils.isBlank(dbkey)) {
            return null;
        }
        return new HashSet<String>(Arrays.asList(dbkey.split(NettyServerConstant.DBKEYS_SPLIT_CHAR)));
    }

    /**
     * Release rpc context.
     *
     * @param channel the channel
     */
    public static void releaseRpcContext(Channel channel) {
        RpcContext rpcContext = getContextFromIdentified(channel);
        if (rpcContext != null) {
            rpcContext.release();
        }
    }

    /**
     * Gets get same income client channel.
     *
     * @param channel the channel
     * @return the get same income client channel
     */
    public static Channel getSameClientChannel(Channel channel) {
        if (channel.isActive()) {
            return channel;
        }
        RpcContext rpcContext = getContextFromIdentified(channel);
        if (rpcContext == null) {
            log.error("rpcContext is null,channel:{},active:{}", channel, channel.isActive());
            return null;
        }
        if (rpcContext.getChannel().isActive()) {
            // recheck
            return rpcContext.getChannel();
        }
        Integer clientPort = ChannelUtil.getClientPortFromChannel(channel);
        String clientIdentified = rpcContext.getApplicationId() + NettyServerConstant.CLIENT_ID_SPLIT_CHAR
                + ChannelUtil.getClientIpFromChannel(channel);
        if (TM_CHANNELS.containsKey(clientIdentified)) {
            ConcurrentMap<Integer, RpcContext> clientRpcMap = TM_CHANNELS.get(clientIdentified);
            return getChannelFromSameClientMap(clientRpcMap, clientPort);
        }
        for (Map<Integer, RpcContext> clientRmMap : rpcContext.getClientRMHolderMap().values()) {
            Channel sameClientChannel = getChannelFromSameClientMap(clientRmMap, clientPort);
            if (sameClientChannel != null) {
                return sameClientChannel;
            }
        }
        return null;

    }

    private static Channel getChannelFromSameClientMap(Map<Integer, RpcContext> clientChannelMap, int exclusivePort) {
        if (clientChannelMap != null && !clientChannelMap.isEmpty()) {
            for (ConcurrentMap.Entry<Integer, RpcContext> entry : clientChannelMap.entrySet()) {
                if (entry.getKey() == exclusivePort) {
                    clientChannelMap.remove(entry.getKey());
                    continue;
                }
                Channel channel = entry.getValue().getChannel();
                if (channel.isActive()) { return channel; }
                clientChannelMap.remove(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Gets get channel.
     *
     * @param resourceId Resource ID
     * @param clientId   Client ID - ApplicationId:IP:Port
     * @return Corresponding channel, NULL if not found.
     */
    public static Channel getChannel(String resourceId, String clientId) {
        Channel resultChannel = null;

        String[] clientIdInfo = readClientId(clientId);

        if (clientIdInfo == null || clientIdInfo.length != 3) {
            throw new RemoteServiceException("Invalid Client ID: " + clientId);
        }

        String targetApplicationId = clientIdInfo[0];
        String targetIP = clientIdInfo[1];
        int targetPort = Integer.parseInt(clientIdInfo[2]);

        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Integer,
                RpcContext>>> applicationIdMap = RM_CHANNELS.get(resourceId);

        if (targetApplicationId == null || applicationIdMap == null ||  applicationIdMap.isEmpty()) {
            log.info("No channel is available for resource[{}]", resourceId);
            return null;
        }

        ConcurrentMap<String, ConcurrentMap<Integer, RpcContext>> ipMap = applicationIdMap.get(targetApplicationId);

        if (ipMap != null && !ipMap.isEmpty()) {
            // Firstly, try to find the original channel through which the branch was registered.
            ConcurrentMap<Integer, RpcContext> portMapOnTargetIP = ipMap.get(targetIP);
            if (portMapOnTargetIP != null && !portMapOnTargetIP.isEmpty()) {
                RpcContext exactRpcContext = portMapOnTargetIP.get(targetPort);
                if (exactRpcContext != null) {
                    Channel channel = exactRpcContext.getChannel();
                    if (channel.isActive()) {
                        resultChannel = channel;
                        log.info("Just got exactly the one {} for {}", channel, clientId);
                    } else {
                        if (portMapOnTargetIP.remove(targetPort, exactRpcContext)) {
                            log.info("Removed inactive {}", channel);
                        }
                    }
                }

                // The original channel was broken, try another one.
                if (resultChannel == null) {
                    for (ConcurrentMap.Entry<Integer, RpcContext> portMapOnTargetIPEntry : portMapOnTargetIP
                            .entrySet()) {
                        Channel channel = portMapOnTargetIPEntry.getValue().getChannel();

                        if (channel.isActive()) {
                            resultChannel = channel;
                            log.info("Choose {} on the same IP[{}] as alternative of {}", channel, targetIP, clientId);
                            break;
                        } else {
                            if (portMapOnTargetIP.remove(portMapOnTargetIPEntry.getKey(),
                                    portMapOnTargetIPEntry.getValue())) {
                                log.info("Removed inactive {}", channel);
                            }
                        }
                    }
                }
            }

            // No channel on the this app node, try another one.
            if (resultChannel == null) {
                for (ConcurrentMap.Entry<String, ConcurrentMap<Integer, RpcContext>> ipMapEntry : ipMap
                        .entrySet()) {
                    if (ipMapEntry.getKey().equals(targetIP)) { continue; }

                    ConcurrentMap<Integer, RpcContext> portMapOnOtherIP = ipMapEntry.getValue();
                    if (portMapOnOtherIP == null || portMapOnOtherIP.isEmpty()) {
                        continue;
                    }

                    for (ConcurrentMap.Entry<Integer, RpcContext> portMapOnOtherIPEntry : portMapOnOtherIP.entrySet()) {
                        Channel channel = portMapOnOtherIPEntry.getValue().getChannel();

                        if (channel.isActive()) {
                            resultChannel = channel;
                            log.info("Choose {} on the same application[{}] as alternative of {}", channel, targetApplicationId, clientId);
                            break;
                        } else {
                            if (portMapOnOtherIP.remove(portMapOnOtherIPEntry.getKey(),
                                    portMapOnOtherIPEntry.getValue())) {
                                log.info("Removed inactive {}", channel);
                            }
                        }
                    }
                    if (resultChannel != null) { break; }
                }
            }
        }

        if (resultChannel == null) {
            resultChannel = tryOtherApp(applicationIdMap, targetApplicationId);

            if (resultChannel == null) {
                log.info("No channel is available for resource[{}] as alternative of {}", resourceId, clientId);
            } else {
                log.info("Choose {} on the same resource[{}] as alternative of {}", resultChannel, resourceId, clientId);
            }
        }

        return resultChannel;

    }

    private static Channel tryOtherApp(ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Integer,
            RpcContext>>> applicationIdMap, String myApplicationId) {
        Channel chosenChannel = null;
        for (ConcurrentMap.Entry<String, ConcurrentMap<String, ConcurrentMap<Integer, RpcContext>>> applicationIdMapEntry : applicationIdMap
                .entrySet()) {
            if (!StringUtils.isBlank(myApplicationId) && applicationIdMapEntry.getKey().equals(myApplicationId)) {
                continue;
            }

            ConcurrentMap<String, ConcurrentMap<Integer, RpcContext>> targetIPMap = applicationIdMapEntry.getValue();
            if (targetIPMap == null || targetIPMap.isEmpty()) {
                continue;
            }

            for (ConcurrentMap.Entry<String, ConcurrentMap<Integer, RpcContext>> targetIPMapEntry : targetIPMap
                    .entrySet()) {
                ConcurrentMap<Integer, RpcContext> portMap = targetIPMapEntry.getValue();
                if (portMap == null || portMap.isEmpty()) {
                    continue;
                }

                for (ConcurrentMap.Entry<Integer, RpcContext> portMapEntry : portMap.entrySet()) {
                    Channel channel = portMapEntry.getValue().getChannel();
                    if (channel.isActive()) {
                        chosenChannel = channel;
                        break;
                    } else {
                        if (portMap.remove(portMapEntry.getKey(), portMapEntry.getValue())) {
                            log.info("Removed inactive {}", channel);
                        }
                    }
                }
                if (chosenChannel != null) { break; }
            }
            if (chosenChannel != null) { break; }
        }
        return chosenChannel;

    }

    /**
     * get rm channels
     *
     * @return
     */
    public static Map<String,Channel> getRmChannels() {
        if (RM_CHANNELS.isEmpty()) {
            return null;
        }
        Map<String, Channel> channels = new HashMap<>(RM_CHANNELS.size());
        RM_CHANNELS.forEach((resourceId, value) -> {
            Channel channel = tryOtherApp(value, null);
            if (channel == null) {
                return;
            }
            channels.put(resourceId, channel);
        });
        return channels;
    }
}
