package com.liyz.auth.common.tcp.core.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 9:42
 */
public class MergedWarpMessage extends AbstractMessage implements Serializable, MergeMessage{

    /**
     * The Msgs.
     */
    public List<AbstractMessage> msgs = new ArrayList<>();
    /**
     * The Msg ids.
     */
    public List<Integer> msgIds = new ArrayList<>();

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_SEATA_MERGE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SeataMergeMessage ");
        for (AbstractMessage msg : msgs) {
            sb.append(msg.toString()).append("\n");
        }
        return sb.toString();
    }
}
