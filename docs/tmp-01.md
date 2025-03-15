


如下表达式中的'1'被当作了char类型
```text
sex == '1'
```

```text
ognl.OgnlOps.isEqual

public static boolean isEqual(Object object1, Object object2) {
    boolean result = false;

    if (object1 == object2) {
        result = true;
    } else if (object1 != null && object2 != null) {
        if (object1.getClass().isArray()) {
            if (object2.getClass().isArray() && (object2.getClass() == object1.getClass())) {
                result = (Array.getLength(object1) == Array.getLength(object2));
                if (result) {
                    for (int i = 0, icount = Array.getLength(object1); result && (i < icount); i++) {
                        result = isEqual(Array.get(object1, i), Array.get(object2, i));
                    }
                }
            }
        } else {
            int t1 = getNumericType(object1);
            int t2 = getNumericType(object2);

            // compare non-comparable non-numeric types by equals only
            if (t1 == NONNUMERIC && t2 == NONNUMERIC && (!(object1 instanceof Comparable) || !(object2 instanceof Comparable))) {
                result = object1.equals(object2);
            } else {
                result = compareWithConversion(object1, object2) == 0;
            }
        }
    }
    return result;
}

public static int getNumericType(Object value) {
    if (value != null) {
        Class<?> c = value.getClass();
        if (c == Integer.class) return INT;
        if (c == Double.class) return DOUBLE;
        if (c == Boolean.class) return BOOL;
        if (c == Byte.class) return BYTE;
        if (c == Character.class) return CHAR;
        if (c == Short.class) return SHORT;
        if (c == Long.class) return LONG;
        if (c == Float.class) return FLOAT;
        if (c == BigInteger.class) return BIGINT;
        if (c == BigDecimal.class) return BIGDEC;
    }
    return NONNUMERIC;
}

```

Compares two objects for equality, even if it has to convert one of them to the other type. 
If both objects are numeric they are converted to the widest type and compared. 
If one is non-numeric and one is numeric the non-numeric is converted to double and compared to the double numeric value. 
If both are non-numeric and Comparable and the types are compatible (i.e. v1 is of the same or superclass of v2's type) they are compared with Comparable.compareTo(). 
If both values are non-numeric and not Comparable or of incompatible classes this will throw and IllegalArgumentException.

```text
// 首先尝试将比较的双方是否能转换为数字进而比较;无法通过数字比较的,就通过compare方法比较
public static int compareWithConversion(Object v1, Object v2) {
    int result;

    if (v1 == v2) {
        result = 0;
    } else {
        int t1 = getNumericType(v1), t2 = getNumericType(v2), type = getNumericType(t1, t2, true);

        switch (type) {
            case BIGINT:
                result = bigIntValue(v1).compareTo(bigIntValue(v2));
                break;

            case BIGDEC:
                result = bigDecValue(v1).compareTo(bigDecValue(v2));
                break;

            case NONNUMERIC:
                if ((t1 == NONNUMERIC) && (t2 == NONNUMERIC)) {
                    if ((v1 instanceof Comparable) && v1.getClass().isAssignableFrom(v2.getClass())) {
                        result = ((Comparable) v1).compareTo(v2);
                        break;
                    } else if ((v1 instanceof Enum<?> && v2 instanceof Enum<?>) &&
                            (v1.getClass() == v2.getClass() || ((Enum) v1).getDeclaringClass() == ((Enum) v2).getDeclaringClass())) {
                        result = ((Enum) v1).compareTo((Enum) v2);
                        break;
                    } else {
                        throw new IllegalArgumentException("invalid comparison: " + v1.getClass().getName() + " and " + v2.getClass().getName());
                    }
                }
                // else fall through
            case FLOAT:
            case DOUBLE:
                double dv1 = doubleValue(v1),
                        dv2 = doubleValue(v2);

                return (dv1 == dv2) ? 0 : ((dv1 < dv2) ? -1 : 1);

            default:
                long lv1 = longValue(v1),
                        lv2 = longValue(v2);

                return (lv1 == lv2) ? 0 : ((lv1 < lv2) ? -1 : 1);
        }
    }
    return result;
}


// 数值类型指的是int/double/byte/char/short/float/bigInt/bigDecimal/boolean,即可以用数字表达含义的
public static int getNumericType(Object value) {
    if (value != null) {
        Class<?> c = value.getClass();
        if (c == Integer.class) return INT;
        if (c == Double.class) return DOUBLE;
        if (c == Boolean.class) return BOOL;
        if (c == Byte.class) return BYTE;
        if (c == Character.class) return CHAR;
        if (c == Short.class) return SHORT;
        if (c == Long.class) return LONG;
        if (c == Float.class) return FLOAT;
        if (c == BigInteger.class) return BIGINT;
        if (c == BigDecimal.class) return BIGDEC;
    }
    return NONNUMERIC;
}

// 如果有一个operand是非数值类型的,就判定为非数值类型的.
public static int getNumericType(int t1, int t2, boolean canBeNonNumeric) {
    if (t1 == t2) return t1;

    if (canBeNonNumeric && (t1 == NONNUMERIC || t2 == NONNUMERIC || t1 == CHAR || t2 == CHAR)) return NONNUMERIC;

    if (t1 == NONNUMERIC) t1 = DOUBLE; // Try to interpret strings as doubles...
    if (t2 == NONNUMERIC) t2 = DOUBLE; // Try to interpret strings as doubles...

    if (t1 >= MIN_REAL_TYPE) {
        if (t2 >= MIN_REAL_TYPE) return Math.max(t1, t2);
        if (t2 < INT) return t1;
        if (t2 == BIGINT) return BIGDEC;
        return Math.max(DOUBLE, t1);
    } else if (t2 >= MIN_REAL_TYPE) {
        if (t1 < INT) return t2;
        if (t1 == BIGINT) return BIGDEC;
        return Math.max(DOUBLE, t2);
    } else return Math.max(t1, t2);
}

//注意: char类型的取值是code point
public static double doubleValue(Object value) throws NumberFormatException {
    if (value == null) return 0.0;
    Class<?> c = value.getClass();
    if (c.getSuperclass() == Number.class) return ((Number) value).doubleValue();
    if (c == Boolean.class) return (Boolean) value ? 1 : 0;
    if (c == Character.class) return (Character) value; //注意: char类型的取值是code point
    String s = stringValue(value, true);

    return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
}
```