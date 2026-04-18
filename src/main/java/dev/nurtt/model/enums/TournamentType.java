package dev.nurtt.model.enums;

public enum TournamentType {
    ROUND_ROBIN,           // круговая лига
    KNOCKOUT,              // на выбывание
    GROUPS_PLAYOFF,        // группы + плей-офф
    DOUBLE_ELIMINATION,    // двойное выбывание
    SINGLE_ELIMINATION,    // простая на выбывание
    SWISS_SYSTEM,          // швейцарская система
    LEAGUE_WITH_PLAYOFF,   // лига + финалы
    BEST_OF_SERIES,        // серия матчей
    TEAM_TOURNAMENT,       // командный
    MIXED,                 // смешанный формат
    PROMOTION_RELEGATION,  // лига с повышением/понижением
    FRIENDLY,              // дружеский
    GUEST_ONLY,            // турнир только для гостей
    JUNIOR,                // юниорский
    OPEN                   // открытый турнир
}
