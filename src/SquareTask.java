import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class SquareTask implements Callable<Map<Double, Double>> {
    private final List<Double> values;

    public SquareTask(List<Double> values) {
        this.values = values;
    }

    @Override
    public Map<Double, Double> call() {
        Map<Double, Double> result = new HashMap<>();
        for (Double value : values) {
            result.put(value, Math.pow(value, 2));
        }
        return result;
    }
}