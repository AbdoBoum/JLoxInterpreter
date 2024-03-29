package com.demointerpreter.interpreter;

import com.demointerpreter.lexical_analyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class Envirenment {
    final Envirenment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    public Envirenment() {
        this.enclosing = null;
    }

    public Envirenment(Envirenment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value, int line) {
        if (!values.containsKey(name)) {
            define(name, value);
            return;
        }
        throw new RuntimeError(new Token(null, name, null, line), "Variable already declared");
    }

    void define(Token name, Object value) {
        if (values.containsKey(name.getText()))
            throw new RuntimeError(name, "variable is already defined.");

        define(name.getText(), value);
    }

    void define(String name, Object value) {
        values.put(name, value);
    }


    public Object get(Token name) {
        if (values.containsKey(name.getText())) {
            return values.get(name.getText());
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.getText() + "'");
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.getText())) {
            values.replace(name.getText(), value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.getText() + "'.");
    }

    public Object getAt(int distance, String name) {
        return ancetor(distance).values.get(name);
    }

    private Envirenment ancetor(int distance) {
        Envirenment envirenment = this;
        for (var i = 0; i < distance; i++) {
            envirenment = envirenment.enclosing;
        }
        return envirenment;
    }

    public void assignAt(int distance, Token name, Object value) {
        ancetor(distance).values.put(name.getText(), value);
    }
}
