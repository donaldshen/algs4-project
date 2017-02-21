import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Bag;

public class BaseballElimination {
    private int n;
    private Team[] teams;
    private int[][] games;
    private int[][] setToEliminate;

    private class Team {
        private String name;
        private int wins;
        private int losses;
        private int remaining;
        private Team(String n, int w, int l, int r) {
            name = n;
            wins = w;
            losses = l;
            remaining = r;
        }
    }

    public BaseballElimination(String filename) {
        storeData(new In(filename));

        setToEliminate = new int[n][];
        for (int i = 0; i < n; i++) {
            setToEliminate[i] = solveElimination(i);
        }
    }

    public int numberOfTeams() {
        return n;
    }

    public Iterable<String> teams() {
        Bag<String> b = new Bag<String>();
        for (Team t : teams) {
            b.add(t.name);
        }
        return b;
    }

    public int wins(String team) {
        return teams[index(team)].wins;
    }

    public int losses(String team) {
        return teams[index(team)].losses;
    }

    public int remaining(String team) {
        return teams[index(team)].remaining;
    }

    public int against(String team1, String team2) {
        return games[index(team1)][index(team2)];
    }

    public boolean isEliminated(String team) {
        return certificateOfElimination(team) != null;
    }
    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        Bag<String> b = new Bag<String>();
        for (int i : setToEliminate[index(team)]) {
            b.add(teams[i].name);
        }
        return b.size() > 0 ? b : null;
    }


    private void storeData(In in) {
        n = in.readInt();
        teams = new Team[n];
        games = new int[n][n];
        for (int i = 0; i < n; i++) {
            String na = in.readString();
            int w = in.readInt();
            int l = in.readInt();
            int r = in.readInt();
            teams[i] = new Team(na, w, l, r);
            for (int j = 0; j < n; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    private int[] solveElimination(int i) {
        int ceiling = teams[i].wins + teams[i].remaining;

        for (int j = 0; j < n; j++) {
            if (ceiling < teams[j].wins) {
                return new int[] {j};
            }
        }

        // n-1个team，n-2+..+1种game，s，t
        int v = n * (n + 1) / 2 + 2;
        int t = v - 1;
        int s = v - 2;
        FlowNetwork network = new FlowNetwork(v);
        for (int j = 0; j < n; j++) {
            if (j != i) {
                network.addEdge(new FlowEdge(j, t, ceiling - teams[j].wins));
                for (int k = j + 1; k < n; k++) {
                    if (k != i) {
                        int gi = gameIndex(j, k);
                        network.addEdge(new FlowEdge(s, gi, games[j][k]));
                        network.addEdge(new FlowEdge(gi, j, Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(gi, k, Double.POSITIVE_INFINITY));
                    }
                }
            }
        }

        Bag<Integer> b = new Bag<Integer>();
        FordFulkerson ff = new FordFulkerson(network, s, t);
        // 当且仅当 s 的邻接边中存在未饱和正向边时，0..n-1 中存在 inCut == true
        for (int j = 0; j < n; j++) {
            if (ff.inCut(j)) {
                b.add(j);
            }
        }

        int[] result = new int[b.size()];
        int j = 0;
        for (int ti : b) {
            result[j++] = ti;
        }
        return result;
    }

    private int gameIndex(int i, int j) {
        return (2 * n - i) * (i + 1) / 2 + j - i - 1;
    }


    private int index(String team) {
        // 用ST可以提升到对数时间
        for (int i = 0; i < n; i++) {
            if (teams[i].name.equals(team)) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
