package org.surfer.strategyprocessor.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static org.surfer.strategyprocessor.model.TimeZone.RUSSIA;
import static org.surfer.strategyprocessor.model.TimeZone.USA;

@Getter
@RequiredArgsConstructor
public enum Share {

    TESLA("BBG000N9MNX3", "Tesla Motors", USA),
    APPLE("BBG000B9XRY4", "Apple", USA),
    AEROFLOT("BBG004S683W7", "Аэрофлот", RUSSIA),
    RUSAL("BBG008F2T3T2", "РУСАЛ", RUSSIA),
    BOEING("BBG000BCSST7", "BOEING", USA),
    SBER("BBG004730N88", "Сбер Банк", RUSSIA),
    ROSNEFT("BBG004731354", "Роснефть", RUSSIA),
    GAZPROM("BBG004730RP0", "Газпром", RUSSIA),
    COCA_COLA("BBG000BMX289", "COCA-COLA", USA),
    JOHNSON_AND_JOHNSON("BBG000BMHYD1", "Johnson & Johnson", USA),
    NETFLIX("BBG000CL9VN6", "Netflix", USA),
    ROSBANK("BBG000PND2J2", "РОСБАНК", RUSSIA),
    POLYMETAL("BBG004PYF2N3", "Polymetal", RUSSIA),
    FOSAGRO("RU000A0JRKT8", "ФосАгро", RUSSIA),
    ALROSA("TCS007252813", "АЛРОСА", RUSSIA),
    MAGNIT("BBG004RVFCY3", "Магнит", RUSSIA),
    YANDEX("TCSS09805522", "Yandex", RUSSIA),
    VK("BBG00178PGX3", "VK", RUSSIA),
    LUKOIL("TCS009024277", "ЛУКОЙЛ", RUSSIA),
    MTS("TCS007775219", "МТС", RUSSIA),
    OZON("TCS4269L1044", "Ozon Holdings PLC", RUSSIA),
    VTB("BBG004730ZJ9", "Банк ВТБ", RUSSIA),
    TATNEFT("BBG004RVFFC0", "Татнефть", RUSSIA),
    MEGAFON("BBG003NXF5V3", "МегаФон", RUSSIA),
    RUB_EUR("BBG00KDWPPW2", "Валютная пара Евро-Рубль", RUSSIA),
    PIK("RU000A0JP7J7", "ПИК", RUSSIA),
    QIWI("BBG005D1WCQ1", "QIWI", RUSSIA),
    TCS_GROUP("BBG00QPYJ5H0", "TCS Group", RUSSIA),
    NOR_NIKEL("BBG004731489", "Норильский никель", RUSSIA),
    ;

    private static final List<Share> CACHE = Arrays.asList(values());
    private final String figi;
    private final String name;
    private final TimeZone timeZone;

    public static List<Share> get() {
        return CACHE;
    }

    public static Share getByFigi(String figi) {
        return CACHE
                .stream()
                .filter(share -> share.getFigi().equalsIgnoreCase(figi))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найдено figi для обработки " + figi));
    }
}
