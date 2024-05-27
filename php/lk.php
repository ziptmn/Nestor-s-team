<?php
session_start();
require_once('db.php');
?>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>< N / T ></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"> <!-- адаптивность контента от Bootstrap5 -->
	  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link rel="stylesheet" href="main.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <?php include 'heder.php';?>
</head>
<body>




<?php
if (isset($_SESSION['authorization']) && $_SESSION['authorization'] == true) {
    // Пользователь авторизован
    if (isset($_SESSION['role'])) {
        $userRole = $_SESSION['role'];
        if ($userRole >= 1 && $userRole <= 9) {
            // echo "Доступ разрешен.";
        } else {
            exit("Доступ запрещен. Ваша роль ($userRole) не позволяет просматривать эту страницу.");
        }
    } else {
        exit("Доступ запрещен. Роль не определена.");
    }
} else {
    // Пользователь НЕ авторизован
    exit("Доступ запрещен. Требуется авторизация.");
}
?>




<body>
    <div class="row justify-content-center">
        <div class="col-lg-6">
          <div class="w-100 p-3">
            <div class="card">
              <div class="card-body">
                
                    <h3>Банковские реквизиты <span class="badge bg-secondary">для тестовой транзакции на 1 рубль</span></h3>   

                    <div id="status">Загрузка статуса...</div>

                    <script>
                        $(document).ready(function () {
                            function checkStatus() {
                                $.ajax({
                                    url: 'status_check.php', // Путь к PHP скрипту, который выведет JSON
                                    type: 'GET',
                                    dataType: 'json',
                                    success: function(data) {
                                        $('#status').text(data.message); 
                                    },
                                    error: function(err) {
                                        $('#status').text('Ошибка при получении данных.');
                                    }
                                });
                            }

                            setInterval(checkStatus, 1000); // Обновляем
                        });
                    </script>









                    
<?php
session_start();
require_once('db.php');

$name_bank = $bik_bank = $namber_bank = $kor_bank = '';
$id_user = isset($_SESSION['id']) ? $_SESSION['id'] : 0;

// Проверка существования данных пользователя
$stmt = $conn->prepare("SELECT `name_bank`, `bik_bank`, `namber_bank`, `kor_bank` FROM `bank_table` WHERE `id_user` = ?");
$stmt->bind_param("i", $id_user);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $name_bank = $row['name_bank'];
    $bik_bank = $row['bik_bank'];
    $namber_bank = $row['namber_bank'];
    $kor_bank = $row['kor_bank'];
}
$stmt->close();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name_bank = mysqli_real_escape_string($conn, $_POST['name_bank']);
    $bik_bank = mysqli_real_escape_string($conn, $_POST['bik_bank']);
    $namber_bank = mysqli_real_escape_string($conn, $_POST['namber_bank']);
    $kor_bank = mysqli_real_escape_string($conn, $_POST['kor_bank']);
    
    if ($result->num_rows > 0) {
        // Обновление существующих данных
        $sql = "UPDATE `bank_table` SET `name_bank` = ?, `bik_bank` = ?, `namber_bank` = ?, `kor_bank` = ? WHERE `id_user` = ?";
    } else {
        // Добавление новых данных
        $sql = "INSERT INTO `bank_table` (`id_user`, `name_bank`, `bik_bank`, `namber_bank`, `kor_bank`, `Confirmed?`) VALUES (?, ?, ?, ?, ?, 'noy')";
    }
    
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("issss", $id_user, $name_bank, $bik_bank, $namber_bank, $kor_bank);
    if ($stmt->execute()) {
        $last_id = $stmt->insert_id;
        echo "<script type='text/javascript'>confirmPayment($last_id);</script>";
    } else {
        echo "Ошибка: " . $stmt->error;
    }
    $stmt->close();
}
?>


<div class="container mt-5">
<!-- Форма -->
<form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
<div class="container mt-5">
    <form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <div class="form-group">
            <label for="name_bank">Название банка:</label>
            <input type="text" class="form-control" id="name_bank" name="name_bank" required placeholder="<?php echo htmlspecialchars($name_bank); ?>">
        </div>
        
        <div class="form-group">
            <label for="bik_bank">БИК банка:</label>
            <input type="text" class="form-control" id="bik_bank" name="bik_bank" required placeholder="<?php echo htmlspecialchars($bik_bank); ?>">
        </div>
        
        <div class="form-group">
            <label for="namber_bank">Номер счета:</label>
            <input type="text" class="form-control" id="namber_bank" name="namber_bank" required placeholder="<?php echo htmlspecialchars($namber_bank); ?>">
        </div>
        
        <div class="form-group">
            <label for="kor_bank">Корреспондентский счет банка:</label>
            <input type="text" class="form-control" id="kor_bank" name="kor_bank" required placeholder="<?php echo htmlspecialchars($kor_bank); ?>">
        </div>
        <br>
        <button type="submit" class="btn btn-primary">Сделать тестовую оплату</button>
    </form>
</div>
</form>
</div>

<script type="text/javascript">
// Функция для установки подтверждения платежа с задержкой в 10 секунд не работает!
function confirmPayment(id) {
    setTimeout(function() {
        // Создание нового XMLHttpRequest объекта для асинхронного запроса к серверу
        var xhr = new XMLHttpRequest();
        // Настройка запроса (метод GET, URL, асинхронность true)
        xhr.open('GET', 'confirm_payment.php?id=' + id, true);
        // Назначение функции обратного вызова для обработки ответа сервера
        xhr.onload = function() {
          // Проверка статуса ответа HTTP (должен быть 200)
          if (xhr.status === 200) {
              // Вывод сообщения с ответом сервера
              alert('Ответ сервера: ' + xhr.responseText);
          } else {
              // Вывод ошибки, если статус ответа не 200
              alert('Ошибка запроса: ' + xhr.status);
          }
        };
        // Отправка запроса
        xhr.send();
    }, 5000); // Задержка 5 секунд
}
</script>


</div>
</div>

</div>
</div>
</div>
</div>
</div>

<!-- Подключаем иконочный шрифт -->
<script src="https://kit.fontawesome.com/92c67e9609.js" crossorigin="anonymous"></script>
<!-- Подключаем от Bootstrap5 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>