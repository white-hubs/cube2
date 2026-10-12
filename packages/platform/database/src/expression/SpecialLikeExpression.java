package com.guhecloud.platform.database.expression;

import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import java.util.Arrays;
import java.util.List;

/**
 * 特殊的StringValue表达式
 * 返回时 不加 '' 引号
 */
@Getter
@Setter
public class SpecialLikeExpression extends ASTNodeAccessImpl implements Expression {

    private String value = "";
    private String prefix = null;
    public static final List<String> ALLOWED_PREFIXES = Arrays.asList("N", "U", "E", "R", "B", "RB", "_utf8");

    public SpecialLikeExpression(String escapedValue) {
        if (escapedValue.startsWith("'") && escapedValue.endsWith("'")) {
            this.value = escapedValue.substring(1, escapedValue.length() - 1);
        } else {
            if (escapedValue.length() > 2) {
                for (var p : ALLOWED_PREFIXES) {
                    if (escapedValue.length() > p.length() && escapedValue.substring(0, p.length()).equalsIgnoreCase(p) &&
                        escapedValue.charAt(p.length()) == '\'') {
                        this.prefix = p;
                        this.value = escapedValue.substring(p.length() + 1, escapedValue.length() - 1);
                        return;
                    }
                }
            }

            this.value = escapedValue;
        }
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(new StringValue(this.value));
    }

    @Override
    public String toString() {
        return (this.prefix != null ? this.prefix : "") + this.value;
    }

    @Override
    public <T, S> T accept(ExpressionVisitor<T> expressionVisitor, S s) {
        return expressionVisitor.visit(new StringValue(this.value), s);
    }
}
