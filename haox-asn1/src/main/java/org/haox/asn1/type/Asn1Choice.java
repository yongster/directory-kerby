package org.haox.asn1.type;

import org.haox.asn1.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Asn1Choice extends AbstractAsn1Type<Asn1Type> {

    private Asn1FieldInfo[] fieldInfos;
    private Asn1Type[] fields;

    public Asn1Choice(Asn1FieldInfo[] fieldInfos) {
        super(TagClass.UNIVERSAL, UniversalTag.CHOICE.getValue());
        setValue(this);
        this.fieldInfos = fieldInfos;
        this.fields = new Asn1Type[fieldInfos.length];
        setEncodingOption(EncodingOption.CONSTRUCTED);
    }

    @Override
    public boolean isConstructed() {
        return true;
    }

    @Override
    protected int encodingBodyLength() {
        AbstractAsn1Type field;
        TaggingOption taggingOption;
        for (int i = 0; i < fields.length; ++i) {
            field = (AbstractAsn1Type) fields[i];
            if (field != null) {
                if (fieldInfos[i].isTagged()) {
                    taggingOption = fieldInfos[i].getTaggingOption();
                    return field.taggedEncodingLength(taggingOption);
                } else {
                    return field.encodingLength();
                }
            }
        }
        return 0;
    }

    @Override
    protected void encodeBody(ByteBuffer buffer) {
        Asn1Type field;
        TaggingOption taggingOption;
        for (int i = 0; i < fields.length; ++i) {
            field = fields[i];
            if (field != null) {
                if (fieldInfos[i].isTagged()) {
                    taggingOption = fieldInfos[i].getTaggingOption();
                    field.taggedEncode(buffer, taggingOption);
                } else {
                    field.encode(buffer);
                }
                break;
            }
        }
    }

    @Override
    protected void decode(LimitedByteBuffer content) throws IOException {
        int foundPos = -1;
        Asn1Item item = decodeOne(content);
        for (int i = 0; i < fieldInfos.length; ++i) {
            if (item.isContextSpecific()) {
                if (fieldInfos[i].getTagNo() == item.tagNo()) {
                    foundPos = i;
                    break;
                }
            } else {
                initField(i);
                if (fields[i].tagFlags() == item.tagFlags() &&
                        fields[i].tagNo() == item.tagNo()) {
                    foundPos = i;
                    break;
                } else {
                    fields[i] = null;
                }
            }
        }
        if (foundPos == -1) {
            throw new RuntimeException("Unexpected item with (tagFlags, tagNo): ("
                    + item.tagFlags() + ", " + item.tagNo() + ")");
        }

        if (! item.isFullyDecoded()) {
            AbstractAsn1Type fieldValue = (AbstractAsn1Type) fields[foundPos];
            if (item.isContextSpecific()) {
                item.decodeValueWith(fieldValue, fieldInfos[foundPos].getTaggingOption());
            } else {
                item.decodeValueWith(fieldValue);
            }
        }
        fields[foundPos] = item.getValue();
    }

    protected void decodeBody(LimitedByteBuffer content) throws IOException {
        // Not used
    }

    private void initField(int idx) {
        try {
            fields[idx] = fieldInfos[idx].getType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Bad field info specified at index of " + idx, e);
        }
    }

    protected <T extends Asn1Type> T getFieldAs(int index, Class<T> t) {
        Asn1Type value = fields[index];
        if (value == null) return null;
        return (T) value;
    }

    protected void setFieldAs(int index, Asn1Type value) {
        fields[index] = value;
    }

    protected String getFieldAsString(int index) {
        Asn1Type value = fields[index];
        if (value == null) return null;

        if (value instanceof Asn1String) {
            return ((Asn1String) value).getValue();
        }

        throw new RuntimeException("The targeted field type isn't of string");
    }

    protected byte[] getFieldAsOctets(int index) {
        Asn1OctetString value = getFieldAs(index, Asn1OctetString.class);
        if (value != null) return value.getValue();
        return null;
    }

    protected void setFieldAsOctets(int index, byte[] bytes) {
        Asn1OctetString value = new Asn1OctetString(bytes);
        setFieldAs(index, value);
    }

    protected Integer getFieldAsInteger(int index) {
        Asn1Integer value = getFieldAs(index, Asn1Integer.class);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }

    protected void setFieldAsInt(int index, int value) {
        setFieldAs(index, new Asn1Integer(value));
    }
}
