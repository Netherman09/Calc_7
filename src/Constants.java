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
        Tangent
    }

    public enum NodeType {
        Normal,
        Special,
        ExponentSpecial,
        SingleSpecial
    }
}
