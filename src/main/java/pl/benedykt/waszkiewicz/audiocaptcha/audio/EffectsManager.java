package pl.benedykt.waszkiewicz.audiocaptcha.audio;


import pl.benedykt.waszkiewicz.audiocaptcha.Configuration;

public class EffectsManager {

    private Configuration configuration;
    int audioSessionId;

    public EffectsManager(int audioSessionId){
        this.audioSessionId = audioSessionId;
        this.configuration= Configuration.getInstance();
    }

    public void applayEffects(){
        if(configuration.getUseDynamicProcessingEffect()){
            EffectsProducer dynamicsProcessing = new DynamicsProcessingProducer();
            dynamicsProcessing.enableEffect(audioSessionId);
        }

        if(configuration.getUsePresetReverbEffect()){
            EffectsProducer presetReverb = new PresetReverbProducer();
            presetReverb.enableEffect(audioSessionId);
        }
    }


}
