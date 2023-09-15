using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace RSBController.Application.Infrastructure.Persistence.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateSequence(
                name: "FarmEventSequence");

            migrationBuilder.CreateTable(
                name: "Accounts",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Username = table.Column<string>(type: "text", nullable: false),
                    Password = table.Column<string>(type: "text", nullable: false),
                    MembershipExpiry = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: true),
                    AccountStatus = table.Column<int>(type: "integer", nullable: false),
                    ScriptAssigment_ScriptId = table.Column<long>(type: "bigint", nullable: true),
                    ScriptAssigment_At = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: true),
                    Discriminator = table.Column<string>(type: "text", nullable: false),
                    ScheduleId = table.Column<long>(type: "bigint", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Accounts", x => x.Id);
                    table.UniqueConstraint("AK_Accounts_Username", x => x.Username);
                });

            migrationBuilder.CreateTable(
                name: "Clients",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    BotId = table.Column<string>(type: "text", nullable: false),
                    StartedAt = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: true),
                    StoppedAt = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: true),
                    LastClientHeartbeat = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: true),
                    Status = table.Column<int>(type: "integer", nullable: false),
                    VncPort = table.Column<int>(type: "integer", nullable: true),
                    WebsocketPort = table.Column<int>(type: "integer", nullable: true),
                    ActiveSession_ScriptId = table.Column<long>(type: "bigint", nullable: false),
                    ActiveSession_AccountId = table.Column<long>(type: "bigint", nullable: false),
                    ActiveSession_StartedAt = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Clients", x => x.Id);
                    table.UniqueConstraint("AK_Clients_BotId", x => x.BotId);
                });

            migrationBuilder.CreateTable(
                name: "ControlStates",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Status = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ControlStates", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "ControlStateStatusChangedEvent",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false, defaultValueSql: "nextval('\"FarmEventSequence\"')"),
                    At = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false),
                    Initiator = table.Column<int>(type: "integer", nullable: false),
                    Status = table.Column<int>(type: "integer", nullable: false),
                    Reason = table.Column<string>(type: "text", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ControlStateStatusChangedEvent", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Schedules",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Schedules", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Scripts",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Name = table.Column<string>(type: "text", nullable: false),
                    Instances = table.Column<int>(type: "integer", nullable: false),
                    Priority = table.Column<int>(type: "integer", nullable: false),
                    ScheduleId = table.Column<long>(type: "bigint", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Scripts", x => x.Id);
                    table.UniqueConstraint("AK_Scripts_Name", x => x.Name);
                });

            migrationBuilder.CreateTable(
                name: "AccountStatusChangedEvent",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false, defaultValueSql: "nextval('\"FarmEventSequence\"')"),
                    At = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false),
                    AccountId = table.Column<long>(type: "bigint", nullable: false),
                    Username = table.Column<string>(type: "text", nullable: false),
                    AccountStatus = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AccountStatusChangedEvent", x => x.Id);
                    table.ForeignKey(
                        name: "FK_AccountStatusChangedEvent_Accounts_AccountId",
                        column: x => x.AccountId,
                        principalTable: "Accounts",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "PlayerDetails",
                columns: table => new
                {
                    AccountId = table.Column<long>(type: "bigint", nullable: false),
                    DisplayName = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PlayerDetails", x => x.AccountId);
                    table.ForeignKey(
                        name: "FK_PlayerDetails_Accounts_AccountId",
                        column: x => x.AccountId,
                        principalTable: "Accounts",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "ClientStatusChangedEvent",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false, defaultValueSql: "nextval('\"FarmEventSequence\"')"),
                    At = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false),
                    ClientId = table.Column<long>(type: "bigint", nullable: false),
                    Status = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ClientStatusChangedEvent", x => x.Id);
                    table.ForeignKey(
                        name: "FK_ClientStatusChangedEvent_Clients_ClientId",
                        column: x => x.ClientId,
                        principalTable: "Clients",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Mules",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    ClientId = table.Column<long>(type: "bigint", nullable: false),
                    Position_X = table.Column<int>(type: "integer", nullable: false),
                    Position_Y = table.Column<int>(type: "integer", nullable: false),
                    Position_Z = table.Column<int>(type: "integer", nullable: false),
                    World = table.Column<int>(type: "integer", nullable: false),
                    Gold = table.Column<int>(type: "integer", nullable: false),
                    Active = table.Column<bool>(type: "boolean", nullable: false),
                    LastHeartbeat = table.Column<DateTimeOffset>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Mules", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Mules_Clients_ClientId",
                        column: x => x.ClientId,
                        principalTable: "Clients",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "TimeRange",
                columns: table => new
                {
                    ScheduleId = table.Column<long>(type: "bigint", nullable: false),
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TimeRange", x => new { x.ScheduleId, x.Id });
                    table.ForeignKey(
                        name: "FK_TimeRange_Schedules_ScheduleId",
                        column: x => x.ScheduleId,
                        principalTable: "Schedules",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "SkillExperience",
                columns: table => new
                {
                    Skill = table.Column<int>(type: "integer", nullable: false),
                    PlayerDetailsId = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SkillExperience", x => new { x.Skill, x.PlayerDetailsId });
                    table.ForeignKey(
                        name: "FK_SkillExperience_PlayerDetails_PlayerDetailsId",
                        column: x => x.PlayerDetailsId,
                        principalTable: "PlayerDetails",
                        principalColumn: "AccountId",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "MulingRequests",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    AccountId = table.Column<long>(type: "bigint", nullable: false),
                    Amount = table.Column<int>(type: "integer", nullable: false),
                    Completed = table.Column<bool>(type: "boolean", nullable: false),
                    Type = table.Column<int>(type: "integer", nullable: false),
                    ActiveMuleId = table.Column<long>(type: "bigint", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MulingRequests", x => x.Id);
                    table.ForeignKey(
                        name: "FK_MulingRequests_Mules_ActiveMuleId",
                        column: x => x.ActiveMuleId,
                        principalTable: "Mules",
                        principalColumn: "Id");
                });

            migrationBuilder.InsertData(
                table: "ControlStates",
                columns: new[] { "Id", "Status" },
                values: new object[] { 1L, 2 });

            migrationBuilder.InsertData(
                table: "Scripts",
                columns: new[] { "Id", "Instances", "Name", "Priority", "ScheduleId" },
                values: new object[] { 1L, 1, "Muler", 0, null });

            migrationBuilder.CreateIndex(
                name: "IX_AccountStatusChangedEvent_AccountId",
                table: "AccountStatusChangedEvent",
                column: "AccountId");

            migrationBuilder.CreateIndex(
                name: "IX_ClientStatusChangedEvent_ClientId",
                table: "ClientStatusChangedEvent",
                column: "ClientId");

            migrationBuilder.CreateIndex(
                name: "IX_Mules_ClientId",
                table: "Mules",
                column: "ClientId",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_MulingRequests_ActiveMuleId",
                table: "MulingRequests",
                column: "ActiveMuleId");

            migrationBuilder.CreateIndex(
                name: "IX_SkillExperience_PlayerDetailsId",
                table: "SkillExperience",
                column: "PlayerDetailsId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AccountStatusChangedEvent");

            migrationBuilder.DropTable(
                name: "ClientStatusChangedEvent");

            migrationBuilder.DropTable(
                name: "ControlStates");

            migrationBuilder.DropTable(
                name: "ControlStateStatusChangedEvent");

            migrationBuilder.DropTable(
                name: "MulingRequests");

            migrationBuilder.DropTable(
                name: "Scripts");

            migrationBuilder.DropTable(
                name: "SkillExperience");

            migrationBuilder.DropTable(
                name: "TimeRange");

            migrationBuilder.DropTable(
                name: "Mules");

            migrationBuilder.DropTable(
                name: "PlayerDetails");

            migrationBuilder.DropTable(
                name: "Schedules");

            migrationBuilder.DropTable(
                name: "Clients");

            migrationBuilder.DropTable(
                name: "Accounts");

            migrationBuilder.DropSequence(
                name: "FarmEventSequence");
        }
    }
}
