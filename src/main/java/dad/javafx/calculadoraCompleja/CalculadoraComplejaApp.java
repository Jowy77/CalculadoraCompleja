package dad.javafx.calculadoraCompleja;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class CalculadoraComplejaApp extends Application {

	private TextField operandoReal1Text, operandoIma1Text;
	private TextField operandoReal2Text, operandoIma2Text;
	private TextField resultadoRealText, ResultadoImaText;
	private ComboBox<String> SelectorOperandoText;

	private Complejo operando1 = new Complejo();
	private Complejo operando2 = new Complejo();
	private Complejo resultado = new Complejo();
	private StringProperty operacion = new SimpleStringProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Aqui inicializo los componentes de la interfaz
		operandoReal1Text = new TextField();
		operandoReal1Text.setPrefColumnCount(3);

		operandoIma1Text = new TextField();
		operandoIma1Text.setPrefColumnCount(3);

		operandoReal2Text = new TextField();
		operandoReal2Text.setPrefColumnCount(3);

		operandoIma2Text = new TextField();
		operandoIma2Text.setPrefColumnCount(3);

		resultadoRealText = new TextField();
		// Esto es para que no se pueda modificar el resultado,hago lo mismo con el
		// imaginario
		resultadoRealText.setDisable(true);
		resultadoRealText.setPrefColumnCount(3);

		ResultadoImaText = new TextField();
		ResultadoImaText.setDisable(true);
		ResultadoImaText.setPrefColumnCount(3);

		SelectorOperandoText = new ComboBox<>();
		SelectorOperandoText.getItems().addAll("+", "-", "*", "/");

		VBox selectorOpBox = new VBox(SelectorOperandoText);
		selectorOpBox.setAlignment(Pos.CENTER);
		selectorOpBox.setFillWidth(false);

		// Aqui preparo toda la interfaz final,colocando como se indicaba en el diseño
		// de la actividad

		HBox numCompleBox1 = new HBox(5, operandoReal1Text, new Label("+"), operandoIma1Text, new Label("i"));
		HBox numCompleBox2 = new HBox(5, operandoReal2Text, new Label("+"), operandoIma2Text, new Label("i"));
		HBox resultadoBox = new HBox(5, resultadoRealText, new Label("+"), ResultadoImaText, new Label("i"));

		numCompleBox1.setAlignment(Pos.CENTER);
		numCompleBox1.setFillHeight(false);

		numCompleBox2.setAlignment(Pos.CENTER);
		numCompleBox2.setFillHeight(false);

		resultadoBox.setAlignment(Pos.CENTER);
		resultadoBox.setFillHeight(false);

		VBox nBox = new VBox(5, numCompleBox1, numCompleBox2, resultadoBox);
		nBox.setAlignment(Pos.CENTER);

		HBox root = new HBox(5, selectorOpBox, nBox);
		root.setAlignment(Pos.CENTER);
		root.setFillHeight(false);

		Scene scene = new Scene(root, 450, 250);

		primaryStage.setTitle("Calculadora compleja Joel Couto Lugo");
		primaryStage.setScene(scene);
		primaryStage.show();

		// Hacemos los bindings de los textos con los numeros que vamos a usar para
		// operar
		operandoReal1Text.textProperty().bindBidirectional(operando1.realProperty(), new NumberStringConverter());
		operandoIma1Text.textProperty().bindBidirectional(operando1.imaginarioProperty(), new NumberStringConverter());

		operandoReal2Text.textProperty().bindBidirectional(operando2.realProperty(), new NumberStringConverter());
		operandoIma2Text.textProperty().bindBidirectional(operando2.imaginarioProperty(), new NumberStringConverter());

		resultadoRealText.textProperty().bindBidirectional(resultado.realProperty(), new NumberStringConverter());
		ResultadoImaText.textProperty().bindBidirectional(resultado.imaginarioProperty(), new NumberStringConverter());

		// Con esto dependiento de lo que este seleccionado en el combo se hara una
		// operacion u otra instantaneamente
		operacion.bind(SelectorOperandoText.getSelectionModel().selectedItemProperty());
		operacion.addListener((o, lv, nv) -> onOperacionSeleccion(nv));

		// Esto es para que se seleccione el primer simbolo del combo, se tiene que
		// hacer despues del listener
		SelectorOperandoText.getSelectionModel().selectFirst();

	}

	private void onOperacionSeleccion(String nv) {
		switch (nv) {

		case "+":
			resultado.realProperty().bind(operando1.realProperty().add(operando2.realProperty()));
			resultado.imaginarioProperty().bind(operando1.imaginarioProperty().add(operando2.imaginarioProperty()));
			break;

		case "-":
			resultado.realProperty().bind(operando1.realProperty().subtract(operando2.realProperty()));
			resultado.imaginarioProperty()
					.bind(operando1.imaginarioProperty().subtract(operando2.imaginarioProperty()));
			break;

		case "*":
			resultado.realProperty().bind((operando1.realProperty().multiply(operando2.realProperty())
					.subtract(operando1.imaginarioProperty().multiply(operando2.imaginarioProperty()))));

			resultado.imaginarioProperty().bind((operando1.realProperty().multiply(operando2.imaginarioProperty())
					.add(operando1.imaginarioProperty().multiply(operando2.realProperty()))));
			break;

		case "/":

			DoubleProperty x = new SimpleDoubleProperty();
			DoubleProperty y = new SimpleDoubleProperty();

			x.bind((operando1.realProperty().multiply(operando2.realProperty()))
					.add(operando1.imaginarioProperty().multiply(operando2.imaginarioProperty())));

			y.bind((operando2.imaginarioProperty().multiply(operando2.imaginarioProperty()))
					.add(operando2.realProperty().multiply(operando2.realProperty())));

			resultado.realProperty().bind(x.divide(y));

			x = new SimpleDoubleProperty();
			y = new SimpleDoubleProperty();

			x.bind((operando1.imaginarioProperty().multiply(operando2.realProperty()))
					.subtract(operando1.realProperty().multiply(operando2.imaginarioProperty())));

			y.bind((operando2.realProperty().multiply(operando2.realProperty()))
					.add(operando2.imaginarioProperty().multiply(operando2.imaginarioProperty())));

			resultado.imaginarioProperty().bind(x.divide(y));

			break;
		}

	}

	public static void main(String[] args) {

		launch(args);

	}

}
