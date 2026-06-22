package clientMainFiles;

import core.ClientCore;
import graphics.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Dragon;
import network.Response;
import network.UDPClient;
import sharedTools.Arg;
import utils.ConfigManager;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * Main-класс клиента.
 */
public class Main extends Application {

    /**
     * Ядро клиента для работы с командами.
     */
    private static ClientCore clientCore;

    /**
     * Сетевой клиент для работы с сокетом.
     */
    private static UDPClient udpClient;

    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {
        // Считываем конфиг, если передан аргумент
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        }

        try {
            // Инициализируем сетевое взаимодействие и ядро
            udpClient = new UDPClient(ConfigManager.ip, ConfigManager.port);
            clientCore = new ClientCore(udpClient);

            // Запускаем цикл интерфейса
            launch(args);

        } catch (UnknownHostException e) {
            throw new RuntimeException("error.network.unknown_host", e);
        } catch (SocketException e) {
            throw new RuntimeException("error.network.socket_failed", e);
        } catch (Exception e) {
            // Общая критическая ошибка инициализации
            throw new RuntimeException("error.internal.startup_failed", e);
        } finally {
            // Гарантированно закрываем сокет при выходе из приложения
            if (udpClient != null) {
                udpClient.close();
            }
        }
    }

    /**
     * Инициализация и запуск графического интерфейса.
     *
     * @param stage главное окно приложения (предоставляется JavaFX).
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Java & Drakonchiki®");

        Stage authStage = new Stage();
        AuthorizationWindow authorizationWindow = new AuthorizationWindow(authStage);

        startLogin(authorizationWindow);

        if (authorizationWindow.isAuthenticated()) {
            MainWindow mainWindow = new MainWindow(stage);
            setMainButtonsActions(mainWindow);
            mainWindow.show();
        } else {
            Platform.exit();
        }
    }

    private void setMainButtonsActions(MainWindow mw) {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            mw.buttons[i].setOnAction(e -> {
                Stage commandStage = new Stage();
                CommandWindow commandWindow = new CommandWindow(commandStage, mw.buttonsNames[finalI]);
                setCommandWindowButtons(commandWindow, mw);
                commandStage.initModality(Modality.APPLICATION_MODAL);
                commandWindow.show();
            });

        }
        mw.buttons[5].setOnAction(e -> {
            Response response = clientCore.executeCommand("clear", new Arg[0]);
            mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
        });
        mw.buttons[6].setOnAction(e -> {
            Response response = clientCore.executeCommand("remove_head", new Arg[0]);
            mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
        });
        mw.buttons[7].setOnAction(e -> {
            Response response = clientCore.executeCommand("average_of_age", new Arg[0]);
            mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
        });
        mw.buttons[8].setOnAction(e -> {
            Response response = clientCore.executeCommand("print_unique_weight", new Arg[0]);
            mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
        });
        mw.buttons[9].setOnAction(e -> {
            Response response = clientCore.executeCommand("show", new Arg[0]);
            //mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
            Stage showStage = new Stage();
            ShowWindow showWindow = new ShowWindow(showStage);
            if(response.isSuccess()) {
                showWindow.setTableFromCSV(response.getData());
                showStage.initModality(Modality.APPLICATION_MODAL);
                showWindow.show();
            }else{
                mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
            }
        });

    }

    private void setCommandWindowButtons(CommandWindow cw, MainWindow mw) {
        Dragon[] inputtedDragon = new Dragon[1];
        switch (cw.commandName) {
            case "Add":
                cw.enterModel.setOnAction(e -> {
                    Stage inputStage = new Stage();
                    EnterDragonWindow edw = new EnterDragonWindow(inputStage);
                    inputStage.initModality(Modality.APPLICATION_MODAL);
                    inputtedDragon[0] = edw.showAndGetDragon();
                });
                cw.executeButton.setOnAction(e -> {
                    if (inputtedDragon[0] == null) return;
                    Response response = clientCore.executeCommand("add", new Arg[]{new Arg(inputtedDragon[0])});
                    if (response.isSuccess()) {
                        mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
                        cw.close();
                    } else {
                        cw.errorLabel.setText(LocalizationManager.getLocalizedMessage(response.getData()));
                    }
                });
                break;
            case "AddIfMax":
                cw.enterModel.setOnAction(e -> {
                    Stage inputStage = new Stage();
                    EnterDragonWindow edw = new EnterDragonWindow(inputStage);
                    inputStage.initModality(Modality.APPLICATION_MODAL);
                    inputtedDragon[0] = edw.showAndGetDragon();
                });
                cw.executeButton.setOnAction(e -> {
                    if (inputtedDragon[0] == null) return;
                    Response response = clientCore.executeCommand("add_if_max", new Arg[]{new Arg(inputtedDragon[0])});
                    if (response.isSuccess()) {
                        mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
                        cw.close();
                    } else {
                        cw.errorLabel.setText(LocalizationManager.getLocalizedMessage(response.getData()));
                    }
                });
                break;
            case "AddIfMin":
                cw.enterModel.setOnAction(e -> {
                    Stage inputStage = new Stage();
                    EnterDragonWindow edw = new EnterDragonWindow(inputStage);
                    inputStage.initModality(Modality.APPLICATION_MODAL);
                    inputtedDragon[0] = edw.showAndGetDragon();
                });
                cw.executeButton.setOnAction(e -> {
                    if (inputtedDragon[0] == null) return;
                    Response response = clientCore.executeCommand("add_if_min", new Arg[]{new Arg(inputtedDragon[0])});
                    if (response.isSuccess()) {
                        mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
                        cw.close();
                    } else {
                        cw.errorLabel.setText(LocalizationManager.getLocalizedMessage(response.getData()));
                    }
                });
                break;
            case "Update":
                cw.enterModel.setOnAction(e -> {
                    Stage inputStage = new Stage();
                    EnterDragonWindow edw = new EnterDragonWindow(inputStage);
                    inputStage.initModality(Modality.APPLICATION_MODAL);
                    inputtedDragon[0] = edw.showAndGetDragon();
                });
                cw.executeButton.setOnAction(e -> {
                    if (inputtedDragon[0] == null) return;
                    Response response = clientCore.executeCommand("update", new Arg[]{new Arg(cw.argsWindows[0].getText()), new Arg(inputtedDragon[0])});
                    if (response.isSuccess()) {
                        mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
                        cw.close();
                    } else {
                        cw.errorLabel.setText(LocalizationManager.getLocalizedMessage(response.getData()));
                    }
                });
                break;
            case "RemoveById":
                cw.executeButton.setOnAction(e -> {
                    Response response = clientCore.executeCommand("remove_by_id", new Arg[]{new Arg(cw.argsWindows[0].getText())});
                    if (response.isSuccess()) {
                        mw.output.appendText(LocalizationManager.getLocalizedMessage(response.getData()) + '\n');
                        cw.close();
                    } else {
                        cw.errorLabel.setText(LocalizationManager.getLocalizedMessage(response.getData()));
                    }
                });
                break;
        }
    }

    private void startLogin(AuthorizationWindow aw) {
        aw.loginBtn.setOnAction(e -> {
            aw.loginBtn.setDisable(true);
            Arg[] loginArgs = {new Arg(aw.username.getText()), new Arg(aw.password.getText())};
            Response response = clientCore.executeCommand("login", loginArgs);

            if (response.isSuccess()) {
                ConfigManager.login = aw.username.getText();
                ConfigManager.token = response.getData();
                aw.setAuthenticatedSuccess();
            } else {
                String errorText = LocalizationManager.getLocalizedMessage(response.getData());
                aw.error.setText(errorText);
            }
            aw.loginBtn.setDisable(false);
        });

        aw.regBtn.setOnAction(e -> {
            aw.regBtn.setDisable(true);
            Arg[] loginArgs = {new Arg(aw.username.getText()), new Arg(aw.password.getText())};
            Response response = clientCore.executeCommand("register", loginArgs);

            if (response.isSuccess()) {
                ConfigManager.login = aw.username.getText();
                ConfigManager.token = response.getData();
                aw.setAuthenticatedSuccess();
            } else {
                String errorText = LocalizationManager.getLocalizedMessage(response.getData());
                aw.error.setText(errorText);
            }
            aw.regBtn.setDisable(false);
        });

        aw.showAndWait();
    }

    /**
     * Возвращает экземпляр ядра клиента для использования в графических окнах.
     *
     * @return Объект ClientCore.
     */
    public static ClientCore getClientCore() {
        return clientCore;
    }
}