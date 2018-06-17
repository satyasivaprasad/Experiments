package com.newgame.models;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


public class AgentFinalInfoResult {

    @Expose
    private List<AgentInfoResult> AgentInfoResult = new ArrayList<AgentInfoResult>();

    /**
     * @return The AgentInfoResult
     */
    public List<AgentInfoResult> getAgentInfoResult() {
        return AgentInfoResult;
    }

    /**
     * @param AgentInfoResult The AgentInfoResult
     */
    public void setAgentInfoResult(List<AgentInfoResult> AgentInfoResult) {
        this.AgentInfoResult = AgentInfoResult;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
