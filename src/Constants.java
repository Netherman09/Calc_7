public class Constants {
    public enum Precedence {
        LeftToRight,
        Lines,
        Points,
        Exponents,
        Brackets
    }

    public enum Type {
        Addition,
        Subtraction,
        Multiplication,
        Division,
        Exponent,
        Root,
        OpeningBracket,
        ClosingBracket,
        None,
        Value,
        Fraction,
        Point,
        Power,
        Constant,
        Sine,
        Cosine,
        Tangent,
        Var
    }

    public enum NodeType {
        Normal,
        Special,
        ExponentSpecial,
        SingleSpecial
    }

    public enum VarType {
        Ans,
        CustomVar
    }

    public static double lastResult = 0; // Result of the last Evaluation for the Ans Function
    public static double customResult = 0; // Stores the Result of Var
}
