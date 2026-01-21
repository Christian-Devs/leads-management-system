<?php

require __DIR__ . '/../vendor/autoload.php';

use Slim\Factory\AppFactory;
use Dotenv\Dotenv;
use Tian3\PhpBackend\Services\Database;
use Tian3\PhpBackend\Controllers\AuthController;
use Tian3\PhpBackend\Middleware\JwtMiddleware;
use Tian3\PhpBackend\Controllers\LeadsController;

$dotenv = Dotenv::createImmutable(__DIR__ . '/../');
$dotenv->load();

$app = AppFactory::create();
$app->addBodyParsingMiddleware();
$app->addRoutingMiddleware();
$app->addErrorMiddleware(true, true, true);
$app->add(function ($request, $handler) {
    if ($request->getMethod() === 'OPTIONS') {
        $response = new \Slim\Psr7\Response();
    } else {
        $response = $handler->handle($request);
    }

    return $response
        ->withHeader('Access-Control-Allow-Origin', '*')
        ->withHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        ->withHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
});

$app->get('/health', function ($request, $response) {
    $response->getBody()->write(json_encode(['status' => 'ok']));
    return $response->withHeader('Content-Type', 'application/json');
});

$app->get('/health/db', function ($request, $response) {
    try {
        $db = Database::getConnection();
        $stmt = $db->query('SELECT 1');

        $response->getBody()->write(json_encode(['database' => 'connected']));

        return $response->withHeader('Content-Type', 'application/json');
    } catch (Exception $e) {
        $response->getBody()->write(json_encode(['database' => 'error', 'message' => $e->getMessage()]));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(500);
    }
});

$app->post('/api/auth/login', [AuthController::class, 'login']);

$app->group('/api', function ($group) {
    $group->post('/leads', [LeadsController::class, 'create']);
    $group->get('/leads', [LeadsController::class, 'list']);
    $group->get('/leads/{id}', [LeadsController::class, 'get']);
    $group->put('/leads/{id}', [LeadsController::class, 'update']);
    $group->delete('/leads/{id}', [LeadsController::class, 'delete']);
})->add(new JwtMiddleware());

$app->run();
