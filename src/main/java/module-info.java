module ru.netology.cardordering {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.netology.cardordering to javafx.fxml;
    exports ru.netology.cardordering;
}