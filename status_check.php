<?php
session_start();
require_once('db.php');

$response = ['message' => 'Вам нужно войти в систему для отправки реквизитов.', 'confirmed' => false];

if (isset($_SESSION['id'])) {
    $id_user = $_SESSION['id'];

    $stmt = $conn->prepare("SELECT `Confirmed?` FROM `bank_table` WHERE `id_user` = ?");
    $stmt->bind_param("i", $id_user);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $confirmed = $row['Confirmed?'];
        $response = [
            'message' => $confirmed === 'yes' ? "Платеж подтвержден." : "Платеж еще не подтвержден.",
            'confirmed' => $confirmed === 'yes'
        ];
    } else {
        $response['message'] = "Вы еще не отправили реквизиты для тестового платежа.";
    }
    $stmt->close();
}

echo json_encode($response);
exit;
?>