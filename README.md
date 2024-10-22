# Планирование и анализ

# Задание 1.1

[diagram](diagrams/task1/c4_context.puml)

# Задание 1.2 - 1.3

## Диаграммы компонентов

[c4_analytics](diagrams/task2/c4_analytics_class.puml)
[c4_device](diagrams/task2/c4_device_class.puml)
[c4_house](diagrams/task2/c4_house_class.puml)
[c4_notification](diagrams/task2/c4_notification_class.puml)
[c4_telemetry](diagrams/task2/c4_telemetry_class.puml)
[c4_user](diagrams/task2/c4_user_class.puml)

## Диаграммы классов

[c4_analytics](diagrams/task2/c4_analytics_component.puml)
[c4_device](diagrams/task2/c4_device_component.puml)
[c4_house](diagrams/task2/c4_house_component.puml)
[c4_notification](diagrams/task2/c4_notification_component.puml)
[c4_telemetry](diagrams/task2/c4_telemetry_component.puml)
[c4_user](diagrams/task2/c4_user_component.puml)

## Диаграммы отношений сущностей

[er_analytics](diagrams/task3/er_analytics.puml)
[er_device](diagrams/task3/er_device.puml)
[er_house](diagrams/task3/er_house.puml)
[er_notification](diagrams/task3/er_notification.pumlg)
[er_telemetry](diagrams/task3/er_telemetry.puml)
[er_user](diagrams/task3/er_user.puml)

# Задание 1.4

## Сервис управления устройствами

[Спецификация Open API](device_open_api.yaml)

[Спецификация Async API](device_async_api.yaml)

## Сервис телеметрии

[Спецификация Open API](telemetry_open_api.yaml)

[Спецификация Async API](telemetry_async_api.yaml)

# Задание 2.1

## Smart Home Monolith

### Описание

Проект “Smart Home Monolith” представляет собой монолитное приложение для управления отоплением и мониторинга температуры в умном доме. Пользователи могут удаленно включать/выключать отопление, устанавливать желаемую температуру и просматривать текущую температуру через веб-интерфейс.

В дополнение к монолиту, проект включает два микросервиса:

* сервис управления устройствами (device-management-service): отвечает за обработку команд управления устройствами через Kafka. 
* сервис телеметрии (telemetry-service): слушает Kafka-топик sensor-data и обрабатывает данные телеметрии от датчиков.

Сервисы реализованы в упрощённом формате, в целевом решении они взаимодействуют с MQTT брокером для получения телеметрии и отправки команд устройствам, с kafka для взаимодейтствия между собой и другими сервисами.

Монолит на текущий момент не работает с Kafka, в целевой схеме потребуется его доработка, чтобы он начал взаимодействовать через Kafka с другими сервисами.

Все компоненты взаимодействуют с общими зависимостями, такими как PostgreSQL, Zookeeper и Kafka, управляемыми с помощью Docker Compose.

### Пререквизиты

* docker
* docker compose

### Структура проекта

Проект организован в соответствии со стандартной структурой Maven проекта и включает следующие модули:

	•	smart-home-monolith/ - монолитное приложение.
	•	device-management/ - сервис управления устройствами.
	•	telemetry-service/ - сервис обработки телеметрии.
	•	compose.yml - файл Docker Compose для оркестрации всех сервисов.
	•	init.sql - скрипт инициализации баз данных.

### Проверяем работоспособность

```bash
git clone https://github.com/dariatsv/architecture-sprint-3.git
cd architecture-sprint-3
git checkout sprint_3
```

Запуск сервисов:

```bash
docker compose up -d && docker compose logs -f
```

После запуска всех контейнеров выполняем создание устройства:

```bash
curl -v POST http://localhost:8081/api/devices \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Thermostat Living Room",
           "type": "THERMOSTAT",
           "metadata": {
             "location": "Living Room",
             "model": "T1000"
           }
         }'
```

Пример ответа:

```text
{"id":"12d48f01-f085-450c-a83f-ae60d2ef8666","name":"Thermostat Living Room","status":null,"type":"THERMOSTAT","metadata":{"location":"Living Room","model":"T1000"}}
```

Необходимо взять id выше, полученного запроса и подставить в deviceId. В примере 12d48f01-f085-450c-a83f-ae60d2ef8666
Изменение статуса, эмулируем сообщение от монолита:

```bash
 echo '{"deviceId":"12d48f01-f085-450c-a83f-ae60d2ef8666","command":"turn_on"}' | docker exec -i kafka kafka-console-producer.sh --bootstrap-server localhost:9092 --topic device_commands
```

Необходимо взять id выше, полученного запроса и подставить в deviceId. В примере 12d48f01-f085-450c-a83f-ae60d2ef8666
Эмулируем публикацию телеметрии от монолита:

```bash
echo '{"deviceId": "12d48f01-f085-450c-a83f-ae60d2ef8666", "temperature": 25.5}' | docker exec -i kafka kafka-console-producer.sh --bootstrap-server localhost:9092 --topic sensor_data
```

Сервис телеметрии считывает новые данные и сохраняет в базу:

```bash
telemetry-service          | 2024-10-22T18:32:15.749Z  INFO 1 --- [telemetry-service] [ntainer#0-0-C-1] r.y.p.s.t.kafka.TelemetryDataListener    : Received telemetry data: TelemetryDataDTO(deviceId=12d48f01-f085-450c-a83f-ae60d2ef8666, temperature=25.5)
```

### Выключение

```bash
docker compose down -v
```