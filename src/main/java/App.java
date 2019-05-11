import lpij.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
    private static class ValueAndTime<T> {
        final T result;
        final double time;

        ValueAndTime(T result, double time) {
            this.result = result;
            this.time = time;
        }
    }

    private interface Producer<T> {
        T call();
    }

    private static <T> ValueAndTime<T> timeIt(Producer<T> producer) {
        long startTime = System.currentTimeMillis();

        T retVal = producer.call();

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        double elapsedInSeconds = (double) elapsed / 1000.0;

        return new ValueAndTime<>(retVal, elapsedInSeconds);
    }

    private static void runFile(String filename, int[] args) {
        try {
            var input = new String(Files.readAllBytes(Paths.get(filename)));

            var lexer = new Lexer(input);
            var tokens = new ArrayList<Token>();
            while (lexer.hasNext()) {
                tokens.add(lexer.next());
            }

            var program = new Parser(tokens).parseLoopProgram();

            var variableMappings = new VariableMapper(program).mapVariables();

            var interpreter = new Interpreter(program, variableMappings);

            var valAndTime = timeIt(() -> interpreter.execute(args));

            System.out.println("result: " + valAndTime.result);
            System.out.println("execution took " + valAndTime.time + " seconds.");

        } catch (IOException e) {
            System.out.println("could not read from file");
        } catch (ParseException p) {
            System.out.println("error while parsing:");
            System.out.println(p.getStrippedSource());
            for (int i = 0; i < p.getIndex(); i++) {
                System.out.print(" ");
            }
            System.out.println("^");
        }
    }

    private static void printUsage() {
        String sb = "Usage:" + "\n" +
                "loopProgramInterpreter <path to loop-program source> <arguments>";
        System.out.println(sb);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
        } else {
            String file = args[0];

            int[] numberArgs = new int[args.length - 1];
            for (int i = 0; i < numberArgs.length; i++) {
                numberArgs[i] = Integer.parseInt(args[i + 1]);
            }

            runFile(file, numberArgs);
        }
    }
}
