<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Make Order Assignment</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        .container {
            width: 400px;
            margin: 50px auto;
            border: 1px solid #aaa;
            padding: 20px;
            border-radius: 8px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: inline-block;
            width: 100px;
        }

        select {
            width: 200px;
        }

        .radio-group {
            display: flex;
            gap: 20px;
            margin-left: 100px;
        }

        .buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 25px;
        }

        button {
            padding: 8px 12px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <div>Make Order Assignment</div>
        <div>Timer: <span id="timer">00:00:00</span></div>
    </div>

    <form action="assignOrder" method="post">
        <div class="form-group">
            <label for="order">Select Order:</label>
            <select id="order" name="orderId">
                <option value="123">Order #123</option>
                <!-- Thêm các đơn hàng khác tại đây -->
            </select>
        </div>

        <div class="form-group">
            <label for="assignTo">Assign To:</label>
            <select id="assignTo" name="assignee">
                <option value="TomHolland">Tom Holland</option>
                <!-- Thêm các người nhận khác tại đây -->
            </select>
        </div>

        <div class="form-group">
            <label>Priority:</label>
            <div class="radio-group">
                <label><input type="radio" name="priority" value="low"> Low</label>
                <label><input type="radio" name="priority" value="medium"> Medium</label>
                <label><input type="radio" name="priority" value="high"> High</label>
            </div>
        </div>

        <div class="buttons">
            <button type="submit" name="action" value="assign">Assign Now</button>
            <button type="submit" name="action" value="auto">Auto Assign</button>
            <button type="button" onclick="stopTimer()">Stop Timer</button>
        </div>
    </form>
</div>

<script>
    let timer;
    let seconds = 0;

    function startTimer() {
        timer = setInterval(() => {
            seconds++;
            document.getElementById("timer").textContent = new Date(seconds * 1000).toISOString().substr(11, 8);
        }, 1000);
    }

    function stopTimer() {
        clearInterval(timer);
    }

    startTimer();
</script>
</body>
</html>
