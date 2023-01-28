package de.snowii.packeto.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionObject {
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    protected final Object object;
    private final Class<?> clazz;

    public ReflectionObject() {
        object = null;
        clazz = null;
    }

    public ReflectionObject(Object object) {
        this.object = object;
        this.clazz = object.getClass();
    }

    public ReflectionObject(Object object, Class<?> clazz) {
        this.object = object;
        this.clazz = clazz;
    }

    public boolean readBoolean(int index) {
        return read(index, boolean.class);
    }

    public byte readByte(int index) {
        return read(index, byte.class);
    }

    public short readShort(int index) {
        return read(index, short.class);
    }

    public int readInt(int index) {
        return read(index, int.class);
    }

    public long readLong(int index) {
        return read(index, long.class);
    }


    public float readFloat(int index) {
        return read(index, float.class);
    }


    public double readDouble(int index) {
        return read(index, double.class);
    }


    public boolean[] readBooleanArray(int index) {
        return read(index, boolean[].class);
    }


    public byte[] readByteArray(int index) {
        return read(index, byte[].class);
    }


    public short[] readShortArray(int index) {
        return read(index, short[].class);
    }


    public int[] readIntArray(int index) {
        return read(index, int[].class);
    }


    public long[] readLongArray(int index) {
        return read(index, long[].class);
    }


    public float[] readFloatArray(int index) {
        return read(index, float[].class);
    }


    public double[] readDoubleArray(int index) {
        return read(index, double[].class);
    }


    public String[] readStringArray(int index) {
        return read(index, String[].class); // JavaImpact: Can we be sure that returning the original array is okay? retrooper: Yes
    }


    public String readString(int index) {
        return read(index, String.class);
    }


    public Object readAnyObject(int index) {
        try {
            Field f = clazz.getDeclaredFields()[index];
            f.setAccessible(true);
            try {
                return f.get(object);
            } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("Packeto failed to find any field indexed " + index + " in the " + clazz.getSimpleName() + " class!");
        }
        return null;
    }


    public <T> T readObject(int index, Class<? extends T> type) {
        return read(index, type);
    }


    public Enum<?> readEnumConstant(int index, Class<? extends Enum<?>> type) {
        return read(index, type);
    }

    public <T> T read(int index, Class<? extends T> type) {
        try {
            Field field = getField(type, index);
            return (T) field.get(object);
        } catch (IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException("Packeto failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
        }
    }


    public void writeBoolean(int index, boolean value) {
        write(boolean.class, index, value);
    }


    public void writeByte(int index, byte value) {
        write(byte.class, index, value);
    }


    public void writeShort(int index, short value) {
        write(short.class, index, value);
    }


    public void writeInt(int index, int value) {
        write(int.class, index, value);
    }


    public void writeLong(int index, long value) {
        write(long.class, index, value);
    }


    public void writeFloat(int index, float value) {
        write(float.class, index, value);
    }


    public void writeDouble(int index, double value) {
        write(double.class, index, value);
    }


    public void writeString(int index, String value) {
        write(String.class, index, value);
    }


    public void writeObject(int index, Object value) {
        write(value.getClass(), index, value);
    }


    public void writeBooleanArray(int index, boolean[] array) {
        write(boolean[].class, index, array);
    }


    public void writeByteArray(int index, byte[] value) {
        write(byte[].class, index, value);
    }


    public void writeShortArray(int index, short[] value) {
        write(short[].class, index, value);
    }


    public void writeIntArray(int index, int[] value) {
        write(int[].class, index, value);
    }


    public void writeLongArray(int index, long[] value) {
        write(long[].class, index, value);
    }


    public void writeFloatArray(int index, float[] value) {
        write(float[].class, index, value);
    }


    public void writeDoubleArray(int index, double[] value) {
        write(double[].class, index, value);
    }


    public void writeStringArray(int index, String[] value) {
        write(String[].class, index, value);
    }


    public void writeAnyObject(int index, Object value) {
        try {
            Field f = clazz.getDeclaredFields()[index];
            f.set(object, value);
        } catch (Exception e) {
            throw new IllegalStateException("Packeto failed to find any field indexed " + index + " in the " + clazz.getSimpleName() + " class!");
        }
    }


    public void writeEnumConstant(int index, Enum<?> enumConstant) {
        try {
            write(enumConstant.getClass(), index, enumConstant);
        } catch (IllegalStateException ex) {
            write(enumConstant.getDeclaringClass(), index, enumConstant);
        }
    }

    public void write(Class<?> type, int index, Object value) throws IllegalStateException {
        Field field = getField(type, index);
        if (field == null) {
            throw new IllegalStateException("Packeto failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> readList(int index) {
        return read(index, List.class);
    }

    public void writeList(int index, List<?> list) {
        write(List.class, index, list);
    }

    private Field getField(Class<?> type, int index) {
        Map<Class<?>, Field[]> cached = FIELD_CACHE.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        Field[] fields = cached.computeIfAbsent(type, typeClass -> getFields(typeClass, clazz.getDeclaredFields()));
        if (fields.length >= index + 1) {
            return fields[index];
        } else {
            throw new IllegalStateException("Packeto failed to find a " + type.getSimpleName() + " indexed " + index + " by its type in the " + clazz.getName() + " class!");
        }
    }

    private Field[] getFields(Class<?> type, Field[] fields) {
        List<Field> ret = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(type)) {
                field.setAccessible(true);
                ret.add(field);
            }
        }
        return ret.toArray(EMPTY_FIELD_ARRAY);
    }
}
