import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class CopyUtility {
    public <T> T deepCopy(T object) {
        if (object == null) return null;

        // Erstelle die Fabrik für die abstrakte Klasse Node
        RuntimeTypeAdapterFactory<Node> adapterFactory = RuntimeTypeAdapterFactory
                .of(Node.class, "jsonType") // "type" ist der Name im JSON
                .registerSubtype(ValueNode.class, "Value")
                .registerSubtype(AdditionNode.class, "Addition")
                .registerSubtype(SubtractionNode.class, "Subtraction")
                .registerSubtype(MultiplicationNode.class, "Multiplication")
                .registerSubtype(DivisionNode.class, "Division")
                .registerSubtype(FractionNode.class, "Fraction")
                .registerSubtype(RootNode.class, "Root")
                .registerSubtype(PowerNode.class, "Exponent")
                .registerSubtype(SineNode.class, "Sine")
                .registerSubtype(CosineNode.class, "Cosine")
                .registerSubtype(TangentNode.class, "Tangent")
                .registerSubtype(EmptyNode.class, "Empty")
                .registerSubtype(VarNode.class, "Variable")
                .registerSubtype(ConstantsNode.class, "Constant")
                .registerSubtype(PointNode.class, "Point");

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapterFactory).create();
        String jsonString = gson.toJson(object);
        return (T) gson.fromJson(jsonString, object.getClass());
    }
}
