package ksr.pl.kw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import ksr.pl.kw.logic.tank.TankRepository;

import java.net.URL;
import java.util.ResourceBundle;


public class FxUserInterfaceController implements Initializable {

    TankRepository tankRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tankRepository = new TankRepository();
    }

    public void dbTest() {
        System.out.println(tankRepository.getTanksByNation("poland"));
    }
}
