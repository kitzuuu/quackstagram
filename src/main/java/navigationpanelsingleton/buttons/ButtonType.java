package navigationpanelsingleton.buttons;

public enum ButtonType {
    HOME("src/main/resources/navigationpanelsingleton/buttons/HomeButton.png"),
    EXPLORE("src/main/resources/navigationpanelsingleton/buttons/ExploreButton.png"),
    UPLOAD("src/main/resources/navigationpanelsingleton/buttons/UploadButton.png"),
    NOTIFICATIONS("src/main/resources/navigationpanelsingleton/buttons/NotificationsButton.png"),
    PROFILE("src/main/resources/navigationpanelsingleton/buttons/ProfileButton.png");

    private final String iconPath;

    ButtonType(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }

}
