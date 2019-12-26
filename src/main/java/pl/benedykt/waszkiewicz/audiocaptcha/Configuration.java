package pl.benedykt.waszkiewicz.audiocaptcha;

public class Configuration {

    private static Configuration instance;

    private Configuration() {
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Double minColorContrastRatio = 4.0;
    private Double maxColorContrastRatio = 20.0;
    private Integer codeLength = 4;
    private Boolean generateLowerCases = false;
    private Boolean generateUpperCases = false;
    private Boolean generateNumbers = true;
    private Boolean useBlurTextFilter = true;
    private Boolean useDashTextFilter = true;
    private Boolean useDefaultTextFilter = false;
    private Boolean useHollowTextFilter = true;
    private Boolean useTriangleTextFilter = true;

    private Boolean useDynamicProcessingEffect = true;
    private Boolean usePresetReverbEffect = true;


    public static final class Builder {

        private Double minColorContrastRatio = 4.0;
        private Double maxColorContrastRatio = 6.0;
        private Integer codeLength = 4;
        private Boolean generateLowerCases = false;
        private Boolean generateUpperCases = false;
        private Boolean generateNumbers = true;
        private Boolean useBlurTextFilter = true;
        private Boolean useDashTextFilter = true;
        private Boolean useDefaultTextFilter = false;
        private Boolean useHollowTextFilter = true;
        private Boolean useTriangleTextFilter = true;

        private Boolean useDynamicProcessingEffect = true;
        private Boolean usePresetReverbEffect = true;


        public Builder minColorContrastRatio(Double minColorContrastRatio) {
            this.minColorContrastRatio = minColorContrastRatio;
            return this;
        }

        public Builder maxColorContrastRation(Double maxColorContrastRatio) {
            this.maxColorContrastRatio = maxColorContrastRatio;
            return this;
        }

        public Builder generateLowerCases(Boolean generateLowerCases) {
            this.generateLowerCases = generateLowerCases;
            return this;
        }

        public Builder generateNumbers(Boolean generateNumbers) {
            this.generateNumbers = generateNumbers;
            return this;
        }

        public Builder generateUpperCases(Boolean generateUpperCases) {
            this.generateUpperCases = generateUpperCases;
            return this;
        }

        public Builder codeLength(Integer codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        public Builder useBlurTextFilter(Boolean useBlurTextFilter) {
            this.useBlurTextFilter = useBlurTextFilter;
            return this;
        }

        public Builder useDashTextFilter(Boolean useDashTextFilter) {
            this.useDashTextFilter = useDashTextFilter;
            return this;
        }

        public Builder useDefaultTextFilter(Boolean useDefaultTextFilter) {
            this.useDefaultTextFilter = useDefaultTextFilter;
            return this;
        }

        public Builder useHollowTextFilter(Boolean useHollowTextFilter) {
            this.useHollowTextFilter = useHollowTextFilter;
            return this;
        }

        public Builder useTriangleTextFilter(Boolean useTriangleTextFilter) {
            this.useTriangleTextFilter = useTriangleTextFilter;
            return this;
        }

        public Builder useDynamicProcessingEffect(Boolean useDynamicProcessingEffect) {
            this.useDynamicProcessingEffect = useDynamicProcessingEffect;
            return this;
        }

        public Builder usePresetReverbEffect(Boolean usePresetReverbEffect) {
            this.usePresetReverbEffect = usePresetReverbEffect;
            return this;
        }

        public Configuration build() {
            Configuration configuration = getInstance();
            configuration.generateUpperCases = this.generateUpperCases;
            configuration.generateLowerCases = this.generateLowerCases;
            configuration.generateNumbers = this.generateNumbers;
            configuration.minColorContrastRatio = this.minColorContrastRatio;
            configuration.maxColorContrastRatio = this.maxColorContrastRatio;
            configuration.codeLength = this.codeLength;
            configuration.useBlurTextFilter = this.useBlurTextFilter;
            configuration.useDashTextFilter = this.useDashTextFilter;
            configuration.useDefaultTextFilter = this.useDefaultTextFilter;
            configuration.useHollowTextFilter = this.useHollowTextFilter;
            configuration.useTriangleTextFilter = this.useTriangleTextFilter;
            configuration.useDynamicProcessingEffect = this.useDynamicProcessingEffect;
            configuration.usePresetReverbEffect = this.usePresetReverbEffect;
            return configuration;
        }
    }


    public Boolean getGenerateUpperCases() {
        return generateUpperCases;
    }

    public Boolean getGenerateLowerCases() {
        return generateLowerCases;
    }

    public Boolean getGenerateNumbers() {
        return generateNumbers;
    }

    public Double getMinColorContrastRatio() {
        return minColorContrastRatio;
    }

    public Double getMaxColorContrastRatio() {
        return maxColorContrastRatio;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public Boolean getUseBlurTextFilter() {
        return useBlurTextFilter;
    }

    public Boolean getUseDashTextFilter() {
        return useDashTextFilter;
    }

    public Boolean getUseDefaultTextFilter() {
        return useDefaultTextFilter;
    }

    public Boolean getUseHollowTextFilter() {
        return useHollowTextFilter;
    }

    public Boolean getUseTriangleTextFilter() {
        return useTriangleTextFilter;
    }

    public Boolean getUseDynamicProcessingEffect() {
        return useDynamicProcessingEffect;
    }

    public Boolean getUsePresetReverbEffect() {
        return usePresetReverbEffect;
    }
}
