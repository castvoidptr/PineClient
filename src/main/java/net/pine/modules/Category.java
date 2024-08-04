package net.pine.modules;

public enum Category {
    KUUDRA("Kuudra"),
    DUNGEONS("Dungeons"),
    GARDEN("Garden"),
    MINING("Mining"),
    COMBAT("Combat"),
    MACROS("Macros"),
    GENERAL("General"),
    RENDER("Render"),
    PLAYER("Player"),
    WORLD("World"),
    CLIENT("Client"),
    MISC("Misc");

    public String name;

    Category(String name)
    {
        this.name = name;
    }
}
