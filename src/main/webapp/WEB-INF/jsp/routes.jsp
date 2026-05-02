<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список маршрутов</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <header>
        <h1>Учёт маршрутов поездов</h1>
    </header>
    <nav>
        <a href="index.jsp">Главная</a>
        <a href="routes">Все маршруты</a>
    </nav>
    <div class="container">
        <div class="card">
            <h2>Добавить новый маршрут</h2>
            <form action="routes" method="post">
                <div class="form-group">
                    <label>Номер поезда:</label>
                    <input type="text" name="trainNumber" required placeholder="Например, 062М">
                </div>
                <div class="form-group">
                    <label>Название маршрута:</label>
                    <input type="text" name="name" required placeholder="Например, Москва — Владивосток">
                </div>
                <button type="submit" class="btn btn-primary">Создать маршрут</button>
            </form>
        </div>

        <div class="card">
            <h2>Все маршруты</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Номер поезда</th>
                        <th>Название</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="route" items="${routes}">
                        <tr>
                            <td>${route.id}</td>
                            <td><strong>${route.trainNumber}</strong></td>
                            <td>${route.name}</td>
                            <td>
                                <a href="route-details?id=${route.id}" class="btn btn-primary">Детали</a>
                                <a href="routes?action=delete&id=${route.id}" class="btn btn-danger" 
                                   onclick="return confirm('Вы уверены, что хотите удалить маршрут?')">Удалить</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty routes}">
                        <tr>
                            <td colspan="4" style="text-align: center;">Маршрутов пока нет.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
