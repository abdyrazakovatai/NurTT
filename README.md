# NurTT — Tennis Tournament API

## Swagger UI
```
[https://nurtt-production.up.railway.app/swagger-ui/index.html#]
```

---

## Порядок работы

```
1. Создать клуб
2. Создать турнир (нужен clubId)
3. Создать лиги внутри турнира (нужен tournamentId)
4. Зарегистрировать игроков в лиги (нужен leagueId + playerId)
5. Старт турнира → генерируются матчи
6. Вводить результаты матчей
7. Смотреть таблицу мест
```

---

## Эндпоинты

### Club
| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/clubs/create` | Создать клуб |
| GET  | `/api/clubs/{id}`   | Получить клуб |

**Создать клуб:**
```json
POST /api/clubs/create
{
  "name": "Бишкек ТТ Клуб"
}
```

---

### Tournament
| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/tournaments/create` | Создать турнир |
| POST | `/api/tournaments/{id}/start` | Старт турнира |
| GET  | `/api/tournaments/{id}` | Получить турнир |

**Создать турнир:**
```json
POST /api/tournaments/create
{
  "clubId": 1,
  "name": "Сезон 2024",
  "season": 1,
  "tournamentType": "ROUND_ROBIN"
}
```

**Старт турнира:**
```
POST /api/tournaments/1/start
→ Генерирует матчи для всех лиг
→ Статус меняется на IN_PROGRESS
```

---

### League
| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/leagues/createLeague` | Создать лигу |
| GET  | `/api/leagues/{id}` | Получить лигу |

**Создать лигу:**
```json
POST /api/leagues/createLeague
{
  "tournamentId": 1,
  "name": "Super Liga",
  "level": 1
}
```

> level=1 → Super Liga (высшая)
> level=2 → Liga A
> level=3 → Liga B (низшая)

---

### League Slot (игрок в лиге)
| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/league-slots/registerPlayer` | Добавить игрока в лигу |
| GET  | `/api/league-slots/player/{playerId}` | Получить slotId по playerId |

**Добавить игрока:**
```json
POST /api/league-slots/registerPlayer
{
  "leagueId": 1,
  "playerId": 3
}
```

**Получить slotId:**
```
GET /api/league-slots/player/3
→ Нужно чтобы узнать winnerSlotId для ввода результата
```
```json
{
  "playerId": 3,
  "slotId": 7
}
```

---

### Match
| Метод | URL | Описание |
|-------|-----|----------|
| GET  | `/api/matches/{matchId}` | Получить матч |
| GET  | `/api/matches/league/{leagueId}` | Все матчи лиги |
| PUT  | `/api/matches/{matchId}/result` | Ввести результат |

**Все матчи лиги:**
```
GET /api/matches/league/1
→ Показывает все матчи: SCHEDULED и COMPLETED
→ slot1Id и slot2Id нужны для ввода результата
```

**Ввести результат:**
```json
PUT /api/matches/1/result
{
  "winnerSlotId": 3,
  "sets": [
    {"score1": 11, "score2": 5},
    {"score1": 11, "score2": 7},
    {"score1": 11, "score2": 4}
  ]
}
```

> winnerSlotId — это slot1Id или slot2Id из ответа матча (не playerId!)
> Best of 5: до 3 побед (3:0, 3:1, 3:2)

**Ответ:**
```json
{
  "matchId": 1,
  "slot1Id": 1,
  "slot2Id": 3,
  "player1Name": "Nursultan",
  "player2Name": "Atai",
  "winnerName": "Nursultan",
  "slot1Sets": 3,
  "slot2Sets": 0,
  "status": "COMPLETED",
  "ranked": true,
  "sets": [
    {"setNumber": 1, "score1": 11, "score2": 5},
    {"setNumber": 2, "score1": 11, "score2": 7},
    {"setNumber": 3, "score1": 11, "score2": 4}
  ]
}
```

---

### Standings (таблица мест)
| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/standings/league/{leagueId}` | Таблица мест в лиге |

**Ответ:**
```json
[
  {
    "place": 1,
    "playerId": 1,
    "playerName": "Nursultan",
    "wins": 3,
    "losses": 0,
    "setsWon": 9,
    "setsLost": 2,
    "setsDiff": 7,
    "matchesPlayed": 3
  },
  {
    "place": 2,
    "playerId": 3,
    "playerName": "Atai",
    "wins": 2,
    "losses": 1,
    "setsWon": 6,
    "setsLost": 5,
    "setsDiff": 1,
    "matchesPlayed": 3
  }
]
```

> Сортировка: победы → разница партий → личный матч

---

### Rating
| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/rating/leaderboard` | Топ игроков по рейтингу |
| GET | `/api/rating/player/{playerId}/history` | История рейтинга игрока |

**Лидерборд:**
```json
[
  {"playerId": 1, "fullName": "Nursultan", "globalRating": 1250},
  {"playerId": 2, "fullName": "Atai",      "globalRating": 1180}
]
```

**История рейтинга:**
```json
[
  {
    "matchId": 4,
    "oldRating": 1200,
    "newRating": 1241,
    "delta": 41,
    "createdAt": "2024-04-10T10:22:45Z"
  }
]
```

---

## Статусы турнира
| Статус | Описание |
|--------|----------|
| `REGISTRATION` | Идёт набор игроков и лиг |
| `IN_PROGRESS` | Матчи играются |
| `FINISHED` | Турнир завершён |

## Статусы матча
| Статус | Описание |
|--------|----------|
| `SCHEDULED` | Матч запланирован |
| `COMPLETED` | Матч сыгран |
