Процедура Тест()

    Запрос = Новый Запрос;
    Запрос.Текст = "ВЫБРАТЬ Файлы.Файл.Ссылка, // ошибка
                    | Файлы.Файл
                    | ИЗ
                    | СлужебныеФайлы КАК Файлы";
КонецПроцедуры

Процедура Тест2()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТЬ РАЗРЕШЕННЫЕ
    |   СлужебныеФайлы.Файл.Ссылка КАК Ссылка, // ошибка
    |   СлужебныеФайлы.Владелец.Ссылка КАК Владелец // ошибка
    |ИЗ
    |   РегистрСведений.СлужебныеФайлы КАК СлужебныеФайлы
    |ГДЕ
    |   СлужебныеФайлы.Отношение = ЗНАЧЕНИЕ(Перечисление.ОтношенияСлужебныхФайлов.ВизуализацияЭП)";
КонецПроцедуры

Процедура Тест3()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТЬ РАЗРЕШЕННЫЕ
    |   Ссылка.Ссылка КАК Ссылка, // ошибка
    |   СлужебныеФайлы.Владелец КАК Владелец
    |ИЗ
    |   РегистрСведений.СлужебныеФайлы КАК СлужебныеФайлы
    |ГДЕ
    |   СлужебныеФайлы.Отношение = ЗНАЧЕНИЕ(Перечисление.ОтношенияСлужебныхФайлов.ВизуализацияЭП)";
КонецПроцедуры

Процедура Тест4()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТЬ
    |   Документ1.Файл.Ссылка КАК Ссылка,           // ошибка
    |   Документ2.Контрагент.Ссылка КАК Контрагент  // ошибка
    |ИЗ
    |   Документ.Документ1 КАК Документ1
    |       ЛЕВОЕ СОЕДИНЕНИЕ Документ.Документ2 КАК Документ2
    |       ПО Документ1.Ссылка = Документ2.Ссылка";
КонецПроцедуры

Процедура Тест5()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТЬ *
	|ИЗ
	|   Документ.Документ1 КАК Документ1";
КонецПроцедуры

Процедура Тест6()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТЬ Документ.Документ1.Файл.Ссылка,        // ошибка
    |         Справочник.Справочник1.Файл.Ссылка";  // ошибка
КонецПроцедуры

Процедура Тест7()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТь
    |    Таблица.Ссылка
    |ИЗ
    |    &Таблица КАК Таблица"
КонецПроцедуры

Процедура Тест8()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТь
    |    Таблица.Ссылка
    |ИЗ
    |    Справочник." + ИмяСправочника + " КАК Таблица"
КонецПроцедуры

Процедура Тест9()
    Запрос = Новый Запрос;
    Запрос.Текст =
    "ВЫБРАТь
    |    Таблица.Ссылка.Ссылка
    |ИЗ
    |    %1 КАК Таблица"
КонецПроцедуры