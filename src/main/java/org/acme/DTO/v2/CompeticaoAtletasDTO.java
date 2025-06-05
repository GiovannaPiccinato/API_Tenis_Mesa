package org.acme.DTO.v2;

import java.util.List;

public class CompeticaoAtletasDTO {
    private List<Long> atletaIds;

    // getters e setters
    public List<Long> getAtletaIds() {
        return atletaIds;
    }

    public void setAtletaIds(List<Long> atletaIds) {
        this.atletaIds = atletaIds;
    }
}