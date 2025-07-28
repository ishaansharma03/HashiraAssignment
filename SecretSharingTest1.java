import java.util.*;

public class SecretSharingTest1 {
    public static void main(String[] args) {
        int k = 3;

        Map<Integer, String[]> input = new HashMap<>();
        input.put(1, new String[]{"10", "4"});
        input.put(2, new String[]{"2", "111"});
        input.put(3, new String[]{"10", "12"});
        input.put(6, new String[]{"4", "213"});

        List<int[]> points = new ArrayList<>();
        for (Map.Entry<Integer, String[]> e : input.entrySet()) {
            int x = e.getKey();
            int base = Integer.parseInt(e.getValue()[0]);
            String value = e.getValue()[1];
            int y = decode(value, base);
            points.add(new int[]{x, y});
        }

        List<List<int[]>> combos = new ArrayList<>();
        generate(points, k, 0, new ArrayList<>(), combos);

        Map<Long, Integer> count = new HashMap<>();
        Map<Long, List<List<int[]>>> match = new HashMap<>();

        for (List<int[]> c : combos) {
            int[] xs = new int[k];
            long[] ys = new long[k];
            for (int i = 0; i < k; i++) {
                xs[i] = c.get(i)[0];
                ys[i] = c.get(i)[1];
            }
            long secret = lagrange(xs, ys);
            count.put(secret, count.getOrDefault(secret, 0) + 1);
            match.putIfAbsent(secret, new ArrayList<>());
            match.get(secret).add(c);
        }

        long finalSecret = 0;
        int max = 0;
        for (Map.Entry<Long, Integer> e : count.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                finalSecret = e.getKey();
            }
        }

        System.out.println(finalSecret); // Should print 1
    }

    public static int decode(String v, int b) {
        int res = 0, power = 1;
        for (int i = v.length() - 1; i >= 0; i--) {
            char ch = v.charAt(i);
            int d = (ch >= '0' && ch <= '9') ? ch - '0' : 10 + (ch - 'a');
            res += d * power;
            power *= b;
        }
        return res;
    }

    public static void generate(List<int[]> src, int k, int idx, List<int[]> curr, List<List<int[]>> out) {
        if (curr.size() == k) {
            out.add(new ArrayList<>(curr));
            return;
        }
        for (int i = idx; i < src.size(); i++) {
            curr.add(src.get(i));
            generate(src, k, i + 1, curr, out);
            curr.remove(curr.size() - 1);
        }
    }

    public static long lagrange(int[] x, long[] y) {
        double total = 0;
        for (int i = 0; i < x.length; i++) {
            double term = y[i];
            for (int j = 0; j < x.length; j++) {
                if (i != j) {
                    term *= (0.0 - x[j]) / (x[i] - x[j]);
                }
            }
            total += term;
        }
        return Math.round(total);
    }
}
