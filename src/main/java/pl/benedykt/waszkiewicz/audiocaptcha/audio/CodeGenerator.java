package pl.benedykt.waszkiewicz.audiocaptcha.audio;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerator {

    private static final Random RAND = new SecureRandom();

    private static CodeGenerator instance;

    private CodeGenerator(){
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static CodeGenerator getInstance() {
        if(instance == null ){
            instance = new CodeGenerator();
        }
        return instance;
    }

    public Integer[] generateInt(){
        Integer toReturn[] = new Integer[4];
        for(int i=0;i<4;i++)
            toReturn[i] = RAND.nextInt(10);

        return toReturn;
    }

    public String getSequence(){
        StringBuilder sequence = new StringBuilder();
        Integer[] temp = generateInt();
        sequence.append(' ');
        for(int i=0; i<temp.length;i++ )
        {
            sequence.append(temp[i]);
            sequence.append(' ');
        }
        return sequence.toString();
    }
}
