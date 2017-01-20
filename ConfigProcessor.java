import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Process config to generate final config with parameters applied
 */
final class ConfigProcessor {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Missing config values. Provide semi-colon separated config values");
            return;
        }

        Map<String, List<ParameterizedConfigEntry>> parameterizedConfigProperties = new HashMap<>();
        for (String config : args[0].split(";")) {
            String[] kv = config.split(":");
            ParameterizedConfigEntry pce = new ParameterizedConfigEntry(kv[0], kv[1]);
            List<ParameterizedConfigEntry> pces = parameterizedConfigEntries.get(pce.key);
            if (pces == null) {
                pces = new List<>();
                parameterizedConfigEntries.put(pce.key, pces);
            }

            pce.add(pce);
        }

        Map<String, String> configProperties = new HashMap<>();
        parameterizedConfigProperties.forEach((k,v) -> {
            ParameterizedProperty pp = new ParameterizedPropertyBuilder().addAll(v).build();
        }

        configProperties.forEach((k,v) -> System.out.println("Key: " + k + " Value: " + v));
    }

    private static class Parameter {
        private ParameterType type;
        private String value;

        private Parameter(ParameterType type, String value) {
            this.type = type;
            this.value = value;
        }

        private static Set<Parameter> parseAllKnownParameters(String[] rawParameters) {
            Set<Parameter> parsedParameters = new HashSet<>();
            if (rawParameters == null) {
                return parsedParameters;
            }

            for (String rawParameter : rawParameters) {
                String[] pv = rawParameter.split("=");
                if (pv.length != 2) {
                    continue;
                }

                ParameterType type = ParameterType.fromString(pv[0]);
                if (type == null) {
                    continue;
                }

                parsedParameters.add(new Parameter(type, pv[1].trim());
            }

            return parsedParameters;
        }

        private static enum ParameterType {
            Role("role"),
            Service("service"),

            private String typeString;

            private ParameterType(String typeString) {
                this.typeString = typeString;
            }

            private static ParameterType fromString(String typeString) {
                if (typeString == null) {
                    return null;
                }

                typeString = typeString.trim();
                for (ParameterType pt : ParameterType.values()) {
                    if (typeString.equalsIgnoreCase(pt.typeString)) {
                        return pt;
                    }
                }
            }
        }
    }

    private static class ParameterizedConfigEntry {
        private final String key;
        private final Set<Parameter> parameters;
        private final String value;

        private ParameterizedConfigEntry(String parameterizedKey, String value) {
            string[] kps = parameterizedKey.split("?");
            this.key = kps[0].trim();
            if (kps.length == 1) {
                this.parameters = null;
            } else {
                String[] rawParameters = kps[1].trim().split("&");
                this.parameters = Parameter.parseAllKnownParameters(rawParameters);
            }

            this.value = value;
        }
    }

    private static class ParameterizedProperty {
        private String key;
        private String defaultValue;
        private List<ParameterizedConfigEntry> parameterizedConfigEntries;

        private ParameterizedProperty() {}

        private String getResolvedValue(List<Parameter> envParams) {
            // TODO: Implement
            return "";
        }

        private static class ParameterizedPropertyBuilder {
            private List<ParameterizedConfigEntry> parameterizedConfigEntries;

            private ParameterizedPropertyBuilder() {
                this.parameterizedConfigEntries = new ArrayList<>();
            }

            private ParameterizedPropertyBuilder add(ParameterizedConfigEntry pce) {
                this.parameterizedConfigEntries.add(pce);
                return this;
            }

            private ParameterizedPropertyBuilder addAll(Collection<ParameterizedConfigEntry> pces) {
                for (ParameterizedConfigEntry pce : pces) {
                    this.add(pce);
                }

                return this;
            }

            private ParameterizedProperty build() {
                ParameterizedProperty pp = new ParameterizedProperty();
                for (ParameterizedConfigEntry pce : this.parameterizedConfigEntries) {
                    if (pp.key == null) {
                        pp.key = pce.key;
                    }

                    if (pce.parameters == null || pce.parameters.size() == 0) {
                        pp.defaultValue = pce.value;
                        break;
                    }
                }

                pp.parameterizedConfigEntries = this.parameterizedConfigEntries;
                return pp;
            }
        }
    }
}

