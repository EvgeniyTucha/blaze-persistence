package com.blazebit.persistence.impl.function.set;

import java.util.ArrayList;
import java.util.List;

import com.blazebit.persistence.impl.DefaultOrderByElement;
import com.blazebit.persistence.spi.DbmsDialect;
import com.blazebit.persistence.spi.FunctionRenderContext;
import com.blazebit.persistence.spi.JpqlFunction;
import com.blazebit.persistence.spi.OrderByElement;
import com.blazebit.persistence.spi.SetOperationType;

/**
 *
 * @author Christian Beikov
 * @since 1.1.0
 */
public class SetFunction implements JpqlFunction {

    protected final SetOperationType type;
    protected final DbmsDialect dbmsDialect;

    public SetFunction(SetOperationType type, DbmsDialect dbmsDialect) {
        // OPERATION(SUBQUERY, ...)
        this.type = type;
        this.dbmsDialect = dbmsDialect;
    }

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return true;
    }

    @Override
    public Class<?> getReturnType(Class<?> firstArgumentType) {
        return firstArgumentType;
    }

    @Override
    public void render(FunctionRenderContext functionRenderContext) {
        if (functionRenderContext.getArgumentsSize() == 0) {
            throw new RuntimeException("The " + type + " function needs at least one argument <sub_query>! args=" + functionRenderContext);
        }
        
        int size = 0;
        Mode mode = Mode.SUBQUERIES;
        List<String> operands = new ArrayList<String>(functionRenderContext.getArgumentsSize());
        List<OrderByElement> orderByElements = new ArrayList<OrderByElement>(0);
        String limit = null;
        String offset = null;
        for (int i = 0; i < functionRenderContext.getArgumentsSize(); i++) {
            String argument = functionRenderContext.getArgument(i);
            
            if ("'ORDER_BY'".equals(argument)) {
                mode = Mode.ORDER_BYS;
                size += argument.length();
            } else if ("'LIMIT'".equals(argument)) {
                mode = Mode.LIMIT;
                size += argument.length();
            } else if ("'OFFSET'".equals(argument)) {
                mode = Mode.OFFSET;
                size += argument.length();
            } else {
                switch (mode) {
                    case SUBQUERIES:
                        size += argument.length();
                        operands.add(argument);
                        break;
                    case ORDER_BYS:
                        size += argument.length() + 30;
                        orderByElements.add(DefaultOrderByElement.fromString(argument, 1, argument.length() - 2));
                        break;
                    case LIMIT:
                        size += argument.length() + 30;
                        limit = argument;
                        break;
                    case OFFSET:
                        size += argument.length() + 30;
                        offset = argument;
                        break;
                }
            }
        }
        
        StringBuilder sqlSb = new StringBuilder(size + functionRenderContext.getArgumentsSize() * 12);
        dbmsDialect.appendSet(sqlSb, type, true, operands, orderByElements, limit, offset);
        functionRenderContext.addChunk(sqlSb.toString());
    }

    private static enum Mode {
        SUBQUERIES,
        ORDER_BYS,
        LIMIT,
        OFFSET;
    }

}