package utils;

public record Group(String chars, boolean isRepeatable, E_CharClass charClass) {

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

}
