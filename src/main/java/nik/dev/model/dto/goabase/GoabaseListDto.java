package nik.dev.model.dto.goabase;

import java.util.ArrayList;

public class GoabaseListDto {
    private ArrayList<GoabasePartyDto> partylist;

    public ArrayList<GoabasePartyDto> getPartylist() {
        return partylist;
    }

    public void setPartylist(ArrayList<GoabasePartyDto> partylist) {
        this.partylist = partylist;
    }
}
