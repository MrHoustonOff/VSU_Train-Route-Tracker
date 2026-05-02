<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Маршрут: ${route.name}</title>
    <link rel="stylesheet" href="css/style.css">
    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
</head>
<body>
    <header>
        <h1>Маршрут: ${route.name} (Поезд №${route.trainNumber})</h1>
    </header>
    <nav>
        <a href="index.jsp">Главная</a>
        <a href="routes">Все маршруты</a>
    </nav>
    
    <div class="container">
        <div class="card">
            <h2>Карта маршрута</h2>
            <div id="map"></div>
        </div>

        <div class="card">
            <h2 id="form-title">Добавить станцию</h2>
            <form action="stations" method="post" id="station-form">
                <input type="hidden" name="action" value="add" id="form-action">
                <input type="hidden" name="routeId" value="${route.id}">
                <input type="hidden" name="id" id="station-id">
                
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label>Название:</label>
                        <input type="text" name="name" id="field-name" required placeholder="Например, Орел">
                    </div>
                    <div class="form-group">
                        <label>Тип:</label>
                        <select name="type" id="field-type">
                            <option value="INTERMEDIATE">Промежуточная</option>
                            <option value="DEPARTURE">Отправление</option>
                            <option value="ARRIVAL">Прибытие</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Прибытие:</label>
                        <input type="time" name="arrivalTime" id="field-arrival">
                    </div>
                    <div class="form-group">
                        <label>Отправление:</label>
                        <input type="time" name="departureTime" id="field-departure">
                    </div>
                    <div class="form-group">
                        <label>День пути:</label>
                        <input type="number" name="dayOffset" id="field-day" value="0" min="0">
                    </div>
                </div>
                <div style="margin-top: 1rem;">
                    <button type="submit" class="btn btn-primary" id="submit-btn">Добавить станцию</button>
                    <button type="button" class="btn btn-danger" id="cancel-btn" style="display:none;" onclick="resetForm()">Отмена</button>
                </div>
            </form>
        </div>

        <div class="card">
            <h2>Список станций</h2>
            <table>
                <thead>
                    <tr>
                        <th>№</th>
                        <th>Станция</th>
                        <th>Прибытие</th>
                        <th>Отправление</th>
                        <th>День</th>
                        <th>Тип</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="station" items="${route.stations}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${station.name}</td>
                            <td>${station.arrivalTime != null ? station.arrivalTime : '—'}</td>
                            <td>${station.departureTime != null ? station.departureTime : '—'}</td>
                            <td>${station.dayOffset}</td>
                            <td>${station.type}</td>
                            <td>
                                <button class="btn btn-primary" onclick="editStation('${station.id}', '${station.name}', '${station.arrivalTime}', '${station.departureTime}', '${station.dayOffset}', '${station.type}')">Изм.</button>
                                <form action="stations" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="routeId" value="${route.id}">
                                    <input type="hidden" name="id" value="${station.id}">
                                    <button type="submit" class="btn btn-danger" 
                                            onclick="return confirm('Удалить станцию?')">Удалить</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Leaflet JS -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <script>
        function editStation(id, name, arrival, departure, day, type) {
            document.getElementById('form-title').innerText = 'Изменить станцию';
            document.getElementById('form-action').value = 'update';
            document.getElementById('station-id').value = id;
            document.getElementById('field-name').value = name;
            document.getElementById('field-type').value = type;
            document.getElementById('field-arrival').value = arrival || '';
            document.getElementById('field-departure').value = departure || '';
            document.getElementById('field-day').value = day;
            document.getElementById('submit-btn').innerText = 'Сохранить изменения';
            document.getElementById('cancel-btn').style.display = 'inline-block';
            window.scrollTo(0, 0); // Прокрутка к форме
        }

        function resetForm() {
            document.getElementById('form-title').innerText = 'Добавить станцию';
            document.getElementById('form-action').value = 'add';
            document.getElementById('station-id').value = '';
            document.getElementById('station-form').reset();
            document.getElementById('submit-btn').innerText = 'Добавить станцию';
            document.getElementById('cancel-btn').style.display = 'none';
        }
        
        // Собираем данные станций из JSP в JS массив
        const stations = [
            <c:forEach var="s" items="${route.stations}" varStatus="loop">
                {
                    name: "${s.name}",
                    lat: ${s.latitude},
                    lng: ${s.longitude},
                    type: "${s.type}",
                    arrival: "${s.arrivalTime != null ? s.arrivalTime : ''}",
                    departure: "${s.departureTime != null ? s.departureTime : ''}"
                }${!loop.last ? ',' : ''}
            </c:forEach>
        ];

        if (stations.length > 0) {
            // Инициализация карты, центрируем на первой станции
            const map = L.map('map').setView([stations[0].lat, stations[0].lng], 5);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            const points = [];
            stations.forEach(s => {
                if (s.lat !== 0 || s.lng !== 0) {
                    const marker = L.marker([s.lat, s.lng]).addTo(map);
                    
                    let popupContent = "<b>" + s.name + "</b><br>" + s.type;
                    if (s.arrival) popupContent += "<br>Прибытие: " + s.arrival;
                    if (s.departure) popupContent += "<br>Отправление: " + s.departure;
                    
                    marker.bindPopup(popupContent);
                    points.push([s.lat, s.lng]);
                }
            });

            // Рисуем линию маршрута
            if (points.length > 1) {
                const polyline = L.polyline(points, {color: 'red', weight: 3}).addTo(map);
                map.fitBounds(polyline.getBounds());
            }
        } else {
            document.getElementById('map').innerHTML = "<p style='text-align:center; padding: 2rem;'>Станции не добавлены или не имеют координат.</p>";
        }
    </script>
</body>
</html>
