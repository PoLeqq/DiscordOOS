package pl.poleq.discordoos.logic;

public enum Permissions
{
    ALL("all"),
    WARN("warn"),
    MUTE("mute"),
    KICK("kick"),
    BAN("ban"),
    PERMISSIONS("perms");

    private final String name;

    Permissions(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
