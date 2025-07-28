import java.util.*;

public class SecretSharingFinal {
    public static void main(String[] args) {
        int k = 7;

        Map<Integer, String[]> input = new HashMap<>();
        input.put(1, new String[]{"6", "13444211440455345511"});
        input.put(2, new String[]{"15", "aed7015a346d63"});
        input.put(3, new String[]{"15", "6aeeb69631c227c"});
        input.put(4, new String[]{"16", "e1b5e05623d881f"});
        input.put(5, new String[]{"8", "316034514573652620673"});
        input.put(6, new String[]{"3", "2122212201122002221120200210011020220200"});
        input.put(7, new String[]{"3", "20120221122211000100210021102001201112121"});
        input.put(8, new String[]{"6", "20220554335330240002224253"});
        input.put(9, new String[]{"12", "45153788322a1255483"});
        input.put(10, new String[]{"7", "1101613130313526312514143"});

        List<long[]> points = new ArrayList<>();
        for (Map.Entry<Integer, String[]> e : input.entrySet()) {
            int x = e.getKey();
            int base = Integer.parseInt(e.getValue()[0]);
            String val = e.getValue()[1];
            long y = baseToDecimal(val, base);
            points.add(new long[]{x, y});
        }

        List<List<long[]>> combinations = new ArrayList<>();
        combine(points, k, 0, new ArrayList<>(), combinations);

        Map<String, Integer> freq = new HashMap<>();
        Map<String, Long> resultMap = new HashMap<>();

        for (List<long[]> combo : combinations) {
            long[] xs = new long[k];
            long[] ys = new long[k];
            for (int i = 0; i < k; i++) {
                xs[i] = combo.get(i)[0];
                ys[i] = combo.get(i)[1];
            }

            Fraction result = lagrange(xs, ys);
            String key = result.toString();

            freq.put(key, freq.getOrDefault(key, 0) + 1);
            if (!resultMap.containsKey(key) && result.den == 1)
                resultMap.put(key, result.num);
        }

        String best = "";
        int max = 0;
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            if (e.getValue() > max && resultMap.containsKey(e.getKey())) {
                max = e.getValue();
                best = e.getKey();
            }
        }

        System.out.println(resultMap.get(best)); // Final secret
    }

    public static long baseToDecimal(String s, int base) {
        long res = 0, pow = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            char ch = Character.toLowerCase(s.charAt(i));
            int d = (ch >= '0' && ch <= '9') ? ch - '0' : 10 + (ch - 'a');
            res += d * pow;
            pow *= base;
        }
        return res;
    }

    public static void combine(List<long[]> arr, int k, int idx, List<long[]> curr, List<List<long[]>> out) {
        if (curr.size() == k) {
            out.add(new ArrayList<>(curr));
            return;
        }
        for (int i = idx; i < arr.size(); i++) {
            curr.add(arr.get(i));
            combine(arr, k, i + 1, curr, out);
            curr.remove(curr.size() - 1);
        }
    }

    public static Fraction lagrange(long[] x, long[] y) {
        Fraction total = new Fraction(0, 1);
        int k = x.length;

        for (int i = 0; i < k; i++) {
            Fraction term = new Fraction(y[i], 1);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term = term.multiply(new Fraction(-x[j], x[i] - x[j]));
                }
            }
            total = total.add(term);
        }

        return total;
    }
}

class Fraction {
    long num, den;

    public Fraction(long n, long d) {
        long g = gcd(Math.abs(n), Math.abs(d));
        num = n / g;
        den = d / g;
        if (den < 0) {
            den *= -1;
            num *= -1;
        }
    }

    Fraction add(Fraction f) {
        long n = this.num * f.den + f.num * this.den;
        long d = this.den * f.den;
        return new Fraction(n, d);
    }

    Fraction multiply(Fraction f) {
        return new Fraction(this.num * f.num, this.den * f.den);
    }

    public String toString() {
        return num + "/" + den;
    }

    static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
