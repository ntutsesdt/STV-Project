package useCase.command;

import adapter.device.DeviceDriver;

public class CommandFactory {
    private DeviceDriver deviceDriver;
    public Command createCommand(String event, String xPath, String parameter) {
        switch (event) {
            case "Click":
                return this.createClickCommand(xPath);
            case "TypeText":
                return this.createTypeTextCommand(xPath, parameter);
            case "Restart":
                return this.createRestartCommand();
            case "LaunchApp":
                return this.createLaunchAppCommand();
            case "Rotate":
                return this.createRotateCommand();
            case "Delete":
                return this.createDeleteCommand(xPath, parameter);
            case "Scroll":
                return this.createScrollToElement(xPath, parameter);
            case "AssertExist":
            return this.createAssertExistCommand(xPath);
            case "AssertText":
            return this.createAssertTextCommand(xPath, parameter);
            case "AssertCount":
            return this.createAssertCounts(xPath, parameter);
            case "AssertActivity":
            return this.createAssertActivityCommand(parameter);
        }

        throw new RuntimeException("Unexpected command type");
    }

    private Command createScrollToElement(String xPath, String parameter) {
        return new ScrollToElementCommand(deviceDriver, xPath, parameter);
    }

    public CommandFactory(DeviceDriver deviceDriver) {
        this.deviceDriver = deviceDriver;
    }

    private Command createClickCommand(String xPath) {
        return new ClickCommand(deviceDriver, xPath);
    }

    private Command createTypeTextCommand(String xPath, String text) {
        return new TypeTextCommand(deviceDriver, xPath, text);
    }

    private Command createRestartCommand() {
        return new RestartCommand(deviceDriver);
    }

    private Command createLaunchAppCommand() {
        return new LaunchAppCommand(deviceDriver);
    }

    private Command createRotateCommand() {
        return new RotateCommand(deviceDriver);
    }

    private Command createDeleteCommand(String xPath, String times) {
        return new DeleteCommand(deviceDriver, xPath, times);
    }

    private Command createAssertExistCommand(String xPath) {
        return new AssertExistCommand(deviceDriver, xPath);
    }

    private Command createAssertTextCommand(String xPath, String text) {
        return new AssertTextCommand(deviceDriver, xPath, text);
    }

    private Command createAssertCounts(String xPath, String count) {
        return new AssertCountCommand(deviceDriver, xPath, count);
    }

    private Command createAssertActivityCommand(String activity) {
        return new AssertActivityCommand(deviceDriver, activity);
    }
}
