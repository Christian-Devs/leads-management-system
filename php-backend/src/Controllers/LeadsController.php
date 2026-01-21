<?php

namespace Tian3\PhpBackend\Controllers;

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Ramsey\Uuid\Uuid;
use Tian3\PhpBackend\Services\Database;

class LeadsController
{
    public static function create(Request $request, Response $response): Response
    {
        $data = (array) $request->getParsedBody();

        if (empty($data['name'])) {
            $response->getBody()->write(json_encode(['error' => 'Name is required']));
            return $response->withHeader('Content-Type', 'application/json')->withStatus(400);
        }

        $db = Database::getConnection();
        $stmt = $db->prepare('INSERT INTO leads (id, name, email, phone, source, created_at) 
                            VALUES (:id, :name, :email, :phone, :source, NOW())');

        $stmt->execute([
            'id' => Uuid::uuid4()->toString(),
            'name' => $data['name'],
            'email' => $data['email'] ?? null,
            'phone' => $data['phone'] ?? null,
            'source' => $data['source'] ?? null,
        ]);

        $response->getBody()->write(json_encode(['message' => 'Lead created successfully']));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(201);
    }

    public static function list(Request $request, Response $response): Response
    {
        $params = $request->getQueryParams();
        $from = $params['from'] ?? null;
        $to = $params['to'] ?? null;

        $db = Database::getConnection();

        $sql = 'SELECT * FROM leads WHERE 1=1';
        $bind = [];

        if ($from) {
            $sql .= ' AND created_at >= :from';
            $bind['from'] = $from;
        }

        if ($to) {
            $sql .= ' AND created_at <= :to';
            $bind['to'] = $to;
        }

        $sql .= ' ORDER BY created_at DESC';

        $stmt = $db->prepare($sql);
        $stmt->execute($bind);

        $leads = $stmt->fetchAll();

        $response->getBody()->write(json_encode($leads));
        return $response->withHeader('Content-Type', 'application/json');
    }

    public static function get(Request $request, Response $response, array $args): Response
    {
        $db = Database::getConnection();

        $stmt = $db->prepare('SELECT * FROM leads WHERE id = :id');
        $stmt->execute(['id' => $args['id']]);
        $lead = $stmt->fetch();

        if (!$lead) {
            return $response->withStatus(404);
        }

        $response->getBody()->write(json_encode($lead));
        return $response->withHeader('Content-Type', 'application/json');
    }

    public static function update(Request $request, Response $response, array $args): Response
    {
        $data = (array) $request->getParsedBody();
        $db = Database::getConnection();

        $stmt = $db->prepare(
            'UPDATE leads
             SET name = :name, email = :email, phone = :phone, source = :source
             WHERE id = :id'
        );

        $stmt->execute([
            'id' => $args['id'],
            'name' => $data['name'] ?? null,
            'email' => $data['email'] ?? null,
            'phone' => $data['phone'] ?? null,
            'source' => $data['source'] ?? null,
        ]);

        return $response->withStatus(204);
    }

    public static function delete(Request $request, Response $response, array $args): Response
    {
        $db = Database::getConnection();

        $stmt = $db->prepare('DELETE FROM leads WHERE id = :id');
        $stmt->execute(['id' => $args['id']]);

        return $response->withStatus(204);
    }
}
