package SETA;

import com.example.grpc.RIdeService;

import java.util.Random;

public class Position {
    private int x, y;
    public static final Position rechargerD1 = new Position(0,0);
    public static final Position rechargerD2 = new Position(0,9);
    public static final Position rechargerD3 = new Position(9,9);
    public static final Position rechargerD4 = new Position(9,0);

    public Position(){}

    public Position(int a, int b){
        this.x = a;
        this.y = b;
    }

    public void Move(int a, int b){
        this.x = a;
        this.y = b;
    }

    public void Move(Position p){
        this.x = p.getX();
        this.y = p.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static float CalculateDistance(Position from, Position to){
        float distance = (float) Math.sqrt(
                Math.pow(to.getX() - from.getX(), 2)
                + Math.pow(to.getY() - from.getY(), 2));

        return distance;
    }

    public static float CalculateDistance(Position from, RIdeService.Position to){
        float distance = (float) Math.sqrt(
                Math.pow(to.getX() - from.getX(), 2)
                + Math.pow(to.getY() - from.getY(), 2));

        return distance;
    }

    public static int CalculateDistrict(Position p){
        int district = -1;

        if(p == null){
            return -1;
        }

        /*
        0,0     0,9
        ---------
        | 1 | 2 |
        ---------
        | 4 | 3 |
        ---------
        9,0     9,9
         */

        if(p.getX() < 5){ // X range(0, 4)
            // District I or IV
            if(p.getY() < 5){
                district = 1;
            } else {
                district = 2;
            }
        } else { // X range(5, 9)
            if(p.getY() < 5){
                district = 4;
            } else {
                district = 3;
            }
        }

        return district;
    }

    public static Position GenerateStartingPosition(){
        // Random number between a set of numbers
        int[] arr = {0, 9};
        Random r = new Random();

        // X
        int randomIndex = r.nextInt(2);
        int randomValX = arr[randomIndex];

        // Y
        randomIndex = r.nextInt(2);
        int randomValY = arr[randomIndex];

        Position p = new Position(randomValX, randomValY);

        return p;
    }

    public static RIdeService.District ConvertDistrict(int dis){
        if(dis == 1){
            return RIdeService.District.D1;
        } else if(dis == 2){
            return RIdeService.District.D2;
        } else if(dis == 3){
            return RIdeService.District.D3;
        } else if(dis == 4){
            return RIdeService.District.D4;
        }

        return RIdeService.District.D1;
    }

    public static int ConvertDistrict(RIdeService.District dis){
        if(dis == RIdeService.District.D1){
            return 1;
        } else if(dis == RIdeService.District.D2){
            return 2;
        } else if(dis == RIdeService.District.D3){
            return 3;
        } else if(dis == RIdeService.District.D4){
            return 4;
        }

        return 1;
    }

    public static Position RechargerPosition(Position p){
        int d = Position.CalculateDistrict(p);

        if(d == 1){
            return rechargerD1;
        } else if(d == 2){
            return rechargerD2;
        } else if(d == 3){
            return rechargerD3;
        } else if(d == 4){
            return rechargerD4;
        }

        return rechargerD1;
    }

    public static Position ConvertPosition(RIdeService.Position positionToConvert){
        Position p = new Position(
                positionToConvert.getX(),
                positionToConvert.getY()
        );

        return p;
    }


}
