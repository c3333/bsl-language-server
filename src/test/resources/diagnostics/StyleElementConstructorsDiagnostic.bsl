Процедура Проверка1()

    Цвет  = Новый Цвет(255, 255, 255);
    Рамка = Новый Рамка(ТипРамки);
    Шрифт = Новый Шрифт();

КонецПроцедуры

Color  = New Color(255, 255, 255);
Border = New Border(BorderType);
Font   = New Font();

Шрифт2 = Новый("Шрифт");
Рамка2 = Новый("Рамка", ТипРамки);
Цвет2  = Новый("Цвет", 255, 255, 255);

Запрос = Новый Запрос();
НоваяСтруктура = Новый Структура("Рамка");
Запрос = Новый Запрос(
    "ВЫБРАТЬ
    |   1 КАК Поле1,
    |   2 КАК Поле2"
);