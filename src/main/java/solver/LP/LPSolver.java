package solver.LP;

import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class LPSolver {

    public Map<Integer, Integer> solve(int[] places, int[] people) {

        try {
            IloCplex cplex = new IloCplex();

            IloIntVar[][] vars = new IloIntVar[places.length][people.length];

            for (int i = 0; i < places.length; i++) {
                for (int j = 0; j < people.length; j++) {
                    vars[i][j] = cplex.intVar(0, 1, "x" + valueOf(i) + valueOf(j));
                }
            }

            //maximális elberszám per helycsoport
            for (int i = 0; i < places.length; i++) {
                IloIntExpr exp = null;
                for (int j = 0; j < people.length; j++) {
                    if (j == 0){ exp = cplex.sum(cplex.prod(people[j], vars[i][j]), vars[i][j]); }
                    else { exp = cplex.sum(exp, cplex.sum(cplex.prod(people[j], vars[i][j]), vars[i][j])); }
                }
                cplex.addLe(cplex.sum(exp, -1), places[i]);
            }

            // Minden nézőcsoportot csak egy helyre ültetünk
            for (int i = 0; i < people.length; i++) {
                IloIntExpr exp = null;
                for (int j = 0; j < places.length; j++) {
                    if (j == 0){ exp = cplex.sum(0, vars[j][i]); }
                    else { exp = cplex.sum(exp, vars[j][i]); }
                }
                cplex.addEq(exp, 1);
            }

            //Célfüggvény az összes változó értékének összege a nézőcsoportok száma
            IloLinearIntExpr objective = cplex.linearIntExpr();
            for (int i = 0; i < places.length; i++) {
                for (int j = 0; j < people.length; j++) {
                    objective.addTerm(1, vars[i][j]);
                }
            }

            cplex.addMinimize(objective);

            Map<Integer, Integer> res = new HashMap();

            System.out.println();

            if(cplex.solve()){
                for (int i = 0; i < places.length; i++) {
                    for (int j = 0; j < people.length; j++) {
                        System.out.print((int)Math.abs(cplex.getValue(vars[i][j])) + "\t");
                        if(cplex.getValue(vars[i][j]) == 1){
                            res.put(j, i);
                        }
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println("Status: " + cplex.getStatus());
                System.out.println("Obj Value: " + cplex.getObjValue());
                System.out.println();
                return res;
            }
            else {
                return null;
            }
        }
        catch (IloException e) {
            e.printStackTrace();
        }
        return null;
    }

}
