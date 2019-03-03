package net.smitpatel.cricketscore;

public class ListItem {
    private String header;
    private String description;
    private String matchId;
    private String details;
    public ListItem(String header, String description, String matchId) {
        this.header = header;
        this.description = description;
        this.matchId = matchId;
        this.details = "";
    }
    public ListItem(String header, String description, String matchId, String details) {
        this.header = header;
        this.description = description;
        this.matchId = matchId;
        this.details = details;
    }
    public String getMatchId() {
        return matchId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
