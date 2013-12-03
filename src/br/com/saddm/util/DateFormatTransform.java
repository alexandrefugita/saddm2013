package br.com.saddm.util;

import java.text.DateFormat;
import java.util.Date;

import org.simpleframework.xml.transform.Transform;

/**
 * Classe auxiliar para traducao dos formatos de data entre a representacao String e as
 * representacoes de alto nivel das classes Date.
 * @author alexandre
 *
 */
public class DateFormatTransform implements Transform<Date> {
    private DateFormat dateFormat;

    public DateFormatTransform(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Date read(String value) throws Exception {
        return dateFormat.parse(value);
    }

    @Override
    public String write(Date value) throws Exception {
        return dateFormat.format(value);
    }
}