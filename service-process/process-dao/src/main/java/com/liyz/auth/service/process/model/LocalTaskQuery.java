//package com.liyz.auth.service.process.model;
//
//import com.liyz.auth.service.process.dao.RunTaskMapper;
//import org.activiti.engine.ActivitiIllegalArgumentException;
//import org.activiti.engine.ManagementService;
//import org.activiti.engine.impl.QueryOperator;
//import org.activiti.engine.impl.QueryVariableValue;
//import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
//import org.activiti.engine.impl.context.Context;
//import org.activiti.engine.impl.interceptor.CommandContext;
//import org.activiti.engine.impl.variable.VariableTypes;
//import org.activiti.engine.task.Task;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * 注释:
// *
// * @author liyangzhen
// * @version 1.0.0
// * @date 2021/8/27 14:59
// */
//public class LocalTaskQuery implements Serializable {
//    private static final long serialVersionUID = -2711411423259722431L;
//
//    private ManagementService managementService;
//
//    private String taskId;
//
//    private String name;
//
//    private String nameLike;
//
//    private String assignee;
//
//    private String candidateUser;
//    private String candidateGroup;
//    private List<String> candidateGroups;
//    private String userIdForCandidateAndAssignee;
//    private boolean bothCandidateAndAssigned = false;
//
//    private String processNameLike;
//
//    private Boolean includeProcessVariables;
//
//    private Boolean includeTaskLocalVariables;
//
//    private int totalCount;
//
//    private int firstResult = 0;
//
//    private int maxResults = Integer.MAX_VALUE;
//
//    public LocalTaskQuery(ManagementService managementService) {
//        this.managementService = managementService;
//    }
//
//    protected boolean orActive;
//    protected List<LocalTaskQuery> orQueryObjects = new ArrayList<>();
//    protected LocalTaskQuery currentOrQueryObject = null;
//
//    List<QueryVariableValue> queryVariableValues = new ArrayList<>();
//
//    List<ProcessDefKeyAndTaskDefKey> processDefKeyAndTaskDefKeys = new ArrayList<>();
//
//
//    public LocalTaskQuery taskId(String taskId) {
//        if (orActive) {
//            currentOrQueryObject.taskId = taskId;
//        } else {
//            this.taskId = taskId;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery name(String name) {
//        if (orActive) {
//            currentOrQueryObject.name = name;
//        } else {
//            this.name = name;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery assignee(String userid) {
//        if (orActive) {
//            currentOrQueryObject.assignee = userid;
//        } else {
//            this.assignee = userid;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery candidateUser(String candidateUser) {
//        if (orActive) {
//            currentOrQueryObject.candidateUser = candidateUser;
//        } else {
//            this.candidateUser = candidateUser;
//        }
//        return this;
//    }
//
//
//    public LocalTaskQuery candidateGroup(String candidateGroup) {
//        if (orActive) {
//            currentOrQueryObject.candidateGroup = candidateGroup;
//        } else {
//            this.candidateGroup = candidateGroup;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery candidateGroups(List<String> candidateGroups) {
//        if (orActive) {
//            currentOrQueryObject.candidateGroups = candidateGroups;
//        } else {
//            this.candidateGroups = candidateGroups;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery taskCandidateOrAssigned(String userIdForCandidateAndAssignee) {
//        if (orActive) {
//            currentOrQueryObject.bothCandidateAndAssigned = true;
//            currentOrQueryObject.userIdForCandidateAndAssignee = userIdForCandidateAndAssignee;
//        } else {
//            this.bothCandidateAndAssigned = true;
//            this.userIdForCandidateAndAssignee = userIdForCandidateAndAssignee;
//        }
//
//        return this;
//    }
//
//    public LocalTaskQuery processNameLike(String processName) {
//        if (orActive) {
//            currentOrQueryObject.processNameLike = processName;
//        } else {
//            this.processNameLike = processName;
//        }
//        return this;
//    }
//
//    public LocalTaskQuery includeVariables(Boolean includeVariables) {
//        this.includeProcessVariables = includeVariables;
//        return this;
//    }
//
//    public LocalTaskQuery includeLocalVariables(Boolean includeLocalVariables) {
//        this.includeTaskLocalVariables = includeLocalVariables;
//        return this;
//    }
//
//
//    public LocalTaskQuery processVariableValueEquals(String variableName, Object variableValue) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(variableName, variableValue, QueryOperator.EQUALS, false);
//        } else {
//            this.addVariable(variableName, variableValue, QueryOperator.EQUALS, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueNotEquals(String variableName, Object variableValue) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false);
//        } else {
//            this.addVariable(variableName, variableValue, QueryOperator.NOT_EQUALS, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueEqualsIgnoreCase(String name, String value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.EQUALS_IGNORE_CASE, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.EQUALS_IGNORE_CASE, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueNotEqualsIgnoreCase(String name, String value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.NOT_EQUALS_IGNORE_CASE, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.NOT_EQUALS_IGNORE_CASE, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueGreaterThan(String name, Object value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.GREATER_THAN, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.GREATER_THAN, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueGreaterThanOrEqual(String name, Object value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.GREATER_THAN_OR_EQUAL, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.GREATER_THAN_OR_EQUAL, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueLessThan(String name, Object value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.LESS_THAN, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.LESS_THAN, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueLessThanOrEqual(String name, Object value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.LESS_THAN_OR_EQUAL, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.LESS_THAN_OR_EQUAL, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueLike(String name, String value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.LIKE, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.LIKE, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processVariableValueLikeIgnoreCase(String name, String value) {
//        if (orActive) {
//            currentOrQueryObject.addVariable(name, value, QueryOperator.LIKE_IGNORE_CASE, false);
//        } else {
//            this.addVariable(name, value, QueryOperator.LIKE_IGNORE_CASE, false);
//        }
//        return this;
//    }
//
//    public LocalTaskQuery processDefKeyAndTaskDefKeys(List<ProcessDefKeyAndTaskDefKey> processDefKeyAndTaskDefKeys) {
//        this.processDefKeyAndTaskDefKeys = processDefKeyAndTaskDefKeys;
//        return this;
//    }
//
//    public int getFirstResult() {
//        return firstResult;
//    }
//
//    public void setFirstResult(int firstResult) {
//        this.firstResult = firstResult;
//    }
//
//    public int getMaxResults() {
//        return maxResults;
//    }
//
//    public void setMaxResults(int maxResults) {
//        this.maxResults = maxResults;
//    }
//
//    public long count(CommandContext commandContext) {
//        return 0;
//    }
//
//    private void addVariable(String name, Object value, QueryOperator operator, boolean localScope) {
//        if (name == null) {
//            throw new ActivitiIllegalArgumentException("name is null");
//        }
//        if (value == null || isBoolean(value)) {
//            // Null-values and booleans can only be used in EQUALS and NOT_EQUALS
//            switch (operator) {
//                case GREATER_THAN:
//                    throw new ActivitiIllegalArgumentException("Booleans and null cannot be used in 'greater than' condition");
//                case LESS_THAN:
//                    throw new ActivitiIllegalArgumentException("Booleans and null cannot be used in 'less than' condition");
//                case GREATER_THAN_OR_EQUAL:
//                    throw new ActivitiIllegalArgumentException("Booleans and null cannot be used in 'greater than or equal' condition");
//                case LESS_THAN_OR_EQUAL:
//                    throw new ActivitiIllegalArgumentException("Booleans and null cannot be used in 'less than or equal' condition");
//            }
//
//            if (operator == QueryOperator.EQUALS_IGNORE_CASE && !(value instanceof String)) {
//                throw new ActivitiIllegalArgumentException("Only string values can be used with 'equals ignore case' condition");
//            }
//
//            if (operator == QueryOperator.NOT_EQUALS_IGNORE_CASE && !(value instanceof String)) {
//                throw new ActivitiIllegalArgumentException("Only string values can be used with 'not equals ignore case' condition");
//            }
//
//            if ((operator == QueryOperator.LIKE || operator == QueryOperator.LIKE_IGNORE_CASE) && !(value instanceof String)) {
//                throw new ActivitiIllegalArgumentException("Only string values can be used with 'like' condition");
//            }
//        }
//        queryVariableValues.add(new QueryVariableValue(name, value, operator, localScope));
//    }
//
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }
//
//    private boolean isBoolean(Object value) {
//        if (value == null) {
//            return false;
//        }
//        return Boolean.class.isAssignableFrom(value.getClass()) || boolean.class.isAssignableFrom(value.getClass());
//    }
//
//    public Boolean getIncludeProcessVariables() {
//        return includeProcessVariables;
//    }
//
//    public Boolean getIncludeTaskLocalVariables() {
//        return includeTaskLocalVariables;
//    }
//
//    public List<Task> listPage(final int firstResult, final int maxResults) {
//        final LocalTaskQuery localTaskQuery = this;
//        return managementService.executeCustomSql(new AbstractCustomSqlExecution<RunTaskMapper, List<Task>>(RunTaskMapper.class) {
//            @Override
//            public List<Task> execute(RunTaskMapper runTask) {
//                VariableTypes types = Context.getProcessEngineConfiguration().getVariableTypes();
//                for (QueryVariableValue var : queryVariableValues) {
//                    var.initialize(types);
//                }
//                List<Task> instanceList = null;
//                if ((localTaskQuery.getIncludeProcessVariables()!=null && localTaskQuery.getIncludeProcessVariables()) || (localTaskQuery.getIncludeTaskLocalVariables()!=null&&localTaskQuery.getIncludeTaskLocalVariables())) {
//                    instanceList = runTask.selectTaskWidthVariablesByQueryCriteriaLocal(localTaskQuery);
//
//                    localTaskQuery.setTotalCount(instanceList.size());
//
//                    if (instanceList != null && !instanceList.isEmpty()) {
//                        if (firstResult > 0) {
//                            if (firstResult <= instanceList.size()) {
//                                int toIndex = firstResult + Math.min(maxResults, instanceList.size() - firstResult);
//                                return instanceList.subList(firstResult, toIndex);
//                            } else {
//                                return Collections.EMPTY_LIST;
//                            }
//                        } else {
//                            int toIndex = Math.min(maxResults, instanceList.size());
//                            return instanceList.subList(0, toIndex);
//                        }
//                    }
//                    return Collections.EMPTY_LIST;
//                } else {
//                    localTaskQuery.setFirstResult(firstResult);
//                    localTaskQuery.setMaxResults(maxResults);
//
//                    localTaskQuery.setTotalCount(-1);
//                    return runTask.selectTaskByQueryCriteriaLocal(localTaskQuery);
//                }
//            }
//        });
//    }
//
//    public int count(){
//        final LocalTaskQuery localTaskQuery = this;
//        return managementService.executeCustomSql(new AbstractCustomSqlExecution<RunTaskMapper, Integer>(RunTaskMapper.class) {
//            @Override
//            public Integer execute(RunTaskMapper runTask) {
//                VariableTypes types = Context.getProcessEngineConfiguration().getVariableTypes();
//                for (QueryVariableValue var : queryVariableValues) {
//                    var.initialize(types);
//                }
//                return runTask.selectTaskByQueryCriteriaLocalCount(localTaskQuery);
//            }
//        });
//    }
//}
