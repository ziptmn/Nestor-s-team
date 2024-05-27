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
    <!-- <link rel="stylesheet" href="main.css" rel="stylesheet"> -->
    <!-- Подключаем иконочный шрифт -->
    <script src="https://kit.fontawesome.com/92c67e9609.js" crossorigin="anonymous"></script>
    <?php include 'heder.php';?>
</head>


<?php
if (isset($_SESSION['authorization']) && $_SESSION['authorization'] == true) {
    // Пользователь авторизован
    if (isset($_SESSION['role'])) {
        $userRole = $_SESSION['role'];
        if ($userRole >= 1 && $userRole <= 9) {
          exit("Вам нужно совершить тестовую транзакцию на 1 рубль. В <a href='lk.php' target='_self'>личном кабинете компании.</a>");
        } else {
            exit("Доступ запрещен. Ваша роль ($userRole) не позволяет просматривать эту страницу.");
        }
    } else {
        exit("Доступ запрещен. Роль не определена.");
    }
}
?>


<br><br><br><br><br><br>

<body>
    <div class="row justify-content-center">
        <div class="col-lg-4">
          <div class="w-100 p-3">
            <div class="card">
              <div class="card-body">
                
                    <h3>Форма регистрации <span class="badge bg-secondary">< N / T ></span></h3>    
                    
                    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                        <li class="nav-item">
                          <button class="nav-link active" id="pills-entrance" data-bs-toggle="pill" data-bs-target="#pills-home" type="button" role="tab" aria-controls="pills-home" aria-selected="true">Вход</button>
                        </li>
                        <li class="nav-item">
                          <button class="nav-link" id="pills-registration" data-bs-toggle="pill" data-bs-target="#registration_area" type="button" role="tab" aria-controls="registration_area" aria-selected="false">Регистрация</button>
                        </li>
                      </ul>
                      <div class="tab-content" id="pills-tabContent">
                        <div class="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-entrance" tabindex="0">
                            <!-- ВХОД -->
                            <form action="login.php" method="post">
                              <div class="input-group mb-3">
                                <span class="input-group-text"><i class="fa-solid fa-at" style="color: #dc3545;"></i></span>
                                <div class="form-floating">
                                  <input type="email" class="form-control" id="floatingInputGroup1" placeholder="xxx1" name="login_l">
                                  <label for="floatingInputGroup1">E-mail адрес</label>
                                </div>
                              </div>

                            <div class="input-group mb-3">
                              <span class="input-group-text"><i class="fa-solid fa-key" style="color: #dc3545;"></i></span>
                              <div class="form-floating">
                                <input type="password" class="form-control" id="floatingInputGroup1" placeholder="xxx2" name="pass">
                                <label for="floatingInputGroup1">Пароль</label>
                              </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Войти</button>
                            </form>
                            <br>
                            <!-- ВХОД-->
                              <?php
                              if (isset($_SESSION['message'])) {
                                  $messageType = isset($_SESSION['message_type']) && $_SESSION['message_type'] === 'success' ? 'success' : 'danger'; // Определяем тип сообщения

                                  echo '<div class="alert alert-' . $messageType . ' d-flex align-items-center" role="alert">';
                                  echo '<div>';
                                  if ($messageType === 'success') {
                                      echo '<i class="fa-solid fa-thumbs-up"></i>';
                                  } else {
                                      echo '<i class="fa-solid fa-circle-xmark"></i>';
                                  }
                                  echo $_SESSION['message'];
                                  echo '</div>';
                                  echo '</div>';
                                  unset($_SESSION['message']);
                              }
                              ?>
                        </div>
                        <div class="tab-pane fade" id="registration_area" role="tabpanel" aria-labelledby="pills-registration" tabindex="0">

<!-- РЕГИСТРАЦИЯ-->
<form action="registr.php" method="post">

<div class="input-group mb-3">
  <span class="input-group-text"><i class="fa-solid fa-at" style="color: #dc3545;"></i></span>
  <div class="form-floating">
    <input type="email" class="form-control" id="floatingInputGroup1" placeholder="xxx1" name="email">
    <label for="floatingInputGroup1">E-mail адрес</label>
  </div>
</div>

<div class="input-group mb-3">
  <span class="input-group-text"><i class="fa-solid fa-key" style="color: #dc3545;"></i></span>
  <div class="form-floating">
    <input type="password" class="form-control" id="floatingInputGroup1" placeholder="xxx2" name="pass">
    <label for="floatingInputGroup1">Пароль</label>
  </div>
</div>

<div class="input-group mb-3">
  <span class="input-group-text"><i class="fa-solid fa-key" style="color: #dc3545;"></i></span>
  <div class="form-floating">
    <input type="password" class="form-control" id="floatingInputGroup1" placeholder="xxx3" name="repeatpass">
    <label for="floatingInputGroup1">Повторите пароль</label>
  </div>
</div>

<div class="form-check mb-3">
  <input type="checkbox" class="form-check-input" id="validationFormCheck1" required>
  <label class="form-check-label" for="validationFormCheck1">Согласен на обработку персональных данных</label>
  <div class="invalid-feedback">Иначе платформой нельзя будет воспользоваться</div>
</div>





<?php
if (isset($_SESSION['message'])) {
    $messageType = isset($_SESSION['message_type']) && $_SESSION['message_type'] === 'success' ? 'success' : 'danger'; // Определяем тип сообщения

    echo '<div class="alert alert-' . $messageType . ' d-flex align-items-center" role="alert">';
    echo '<div>';
    if ($messageType === 'success') {
        echo '<i class="fa-solid fa-thumbs-up"></i>';
    } else {
        echo '<i class="fa-solid fa-circle-xmark"></i>';
    }
    echo $_SESSION['message'];
    echo '</div>';
    echo '</div>';
    unset($_SESSION['message']);
}
?>



<button type="submit" class="btn btn-secondary">Зарегистрироваться</button>
</form> 
                        </div>
                      </div>
              
              </div>
            </div>
          </div>
        </div>
      </div>


    



<!-- Подключаем от Bootstrap5 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>