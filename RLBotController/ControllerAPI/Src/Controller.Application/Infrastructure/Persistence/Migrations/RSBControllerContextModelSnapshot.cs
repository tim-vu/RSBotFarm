﻿// <auto-generated />
using System;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using RSBController.Application.Infrastructure.Persistence;

#nullable disable

namespace RSBController.Application.Infrastructure.Persistence.Migrations
{
    [DbContext(typeof(RSBControllerContext))]
    partial class RSBControllerContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "7.0.5")
                .HasAnnotation("Relational:MaxIdentifierLength", 63);

            NpgsqlModelBuilderExtensions.UseIdentityByDefaultColumns(modelBuilder);

            modelBuilder.HasSequence("FarmEventSequence");

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Account", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<int>("AccountStatus")
                        .HasColumnType("integer");

                    b.Property<string>("Discriminator")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTimeOffset?>("MembershipExpiry")
                        .HasColumnType("timestamp with time zone");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasKey("Id");

                    b.HasAlternateKey("Username");

                    b.ToTable("Accounts");

                    b.HasDiscriminator<string>("Discriminator").HasValue("Account");

                    b.UseTphMappingStrategy();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.ActiveMule", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<bool>("Active")
                        .HasColumnType("boolean");

                    b.Property<long>("ClientId")
                        .HasColumnType("bigint");

                    b.Property<int>("Gold")
                        .HasColumnType("integer");

                    b.Property<DateTimeOffset>("LastHeartbeat")
                        .HasColumnType("timestamp with time zone");

                    b.Property<int>("World")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasIndex("ClientId")
                        .IsUnique();

                    b.ToTable("Mules");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Client", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<string>("BotId")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTimeOffset?>("LastClientHeartbeat")
                        .HasColumnType("timestamp with time zone");

                    b.Property<DateTimeOffset?>("StartedAt")
                        .HasColumnType("timestamp with time zone");

                    b.Property<int>("Status")
                        .HasColumnType("integer");

                    b.Property<DateTimeOffset?>("StoppedAt")
                        .HasColumnType("timestamp with time zone");

                    b.Property<int?>("VncPort")
                        .HasColumnType("integer");

                    b.Property<int?>("WebsocketPort")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasAlternateKey("BotId");

                    b.ToTable("Clients");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.ControlState", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<int>("Status")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.ToTable("ControlStates");

                    b.HasData(
                        new
                        {
                            Id = 1L,
                            Status = 2
                        });
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.FarmEvent", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint")
                        .HasDefaultValueSql("nextval('\"FarmEventSequence\"')");

                    NpgsqlPropertyBuilderExtensions.UseSequence(b.Property<long>("Id"));

                    b.Property<DateTimeOffset>("At")
                        .HasColumnType("timestamp with time zone");

                    b.HasKey("Id");

                    b.ToTable((string)null);

                    b.UseTpcMappingStrategy();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.MulingRequest", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<long>("AccountId")
                        .HasColumnType("bigint");

                    b.Property<long?>("ActiveMuleId")
                        .HasColumnType("bigint");

                    b.Property<int>("Amount")
                        .HasColumnType("integer");

                    b.Property<bool>("Completed")
                        .HasColumnType("boolean");

                    b.Property<int>("Type")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasIndex("ActiveMuleId");

                    b.ToTable("MulingRequests");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.PlayerDetails", b =>
                {
                    b.Property<long>("AccountId")
                        .HasColumnType("bigint");

                    b.Property<string>("DisplayName")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasKey("AccountId");

                    b.ToTable("PlayerDetails");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Schedule", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.HasKey("Id");

                    b.ToTable("Schedules");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Script", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("bigint");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<long>("Id"));

                    b.Property<int>("Instances")
                        .HasColumnType("integer");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<int>("Priority")
                        .HasColumnType("integer");

                    b.Property<long?>("ScheduleId")
                        .HasColumnType("bigint");

                    b.HasKey("Id");

                    b.HasAlternateKey("Name");

                    b.ToTable("Scripts");

                    b.HasData(
                        new
                        {
                            Id = 1L,
                            Instances = 1,
                            Name = "Muler",
                            Priority = 0
                        });
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.MuleAccount", b =>
                {
                    b.HasBaseType("RSBController.Application.Domain.Entities.Account");

                    b.HasDiscriminator().HasValue("MuleAccount");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.WorkerAccount", b =>
                {
                    b.HasBaseType("RSBController.Application.Domain.Entities.Account");

                    b.Property<long?>("ScheduleId")
                        .HasColumnType("bigint");

                    b.HasDiscriminator().HasValue("WorkerAccount");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.AccountStatusChangedEvent", b =>
                {
                    b.HasBaseType("RSBController.Application.Domain.Entities.FarmEvent.FarmEvent");

                    b.Property<long>("AccountId")
                        .HasColumnType("bigint");

                    b.Property<int>("AccountStatus")
                        .HasColumnType("integer");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasIndex("AccountId");

                    b.ToTable("AccountStatusChangedEvent");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.ClientStatusChangedEvent", b =>
                {
                    b.HasBaseType("RSBController.Application.Domain.Entities.FarmEvent.FarmEvent");

                    b.Property<long>("ClientId")
                        .HasColumnType("bigint");

                    b.Property<int>("Status")
                        .HasColumnType("integer");

                    b.HasIndex("ClientId");

                    b.ToTable("ClientStatusChangedEvent");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.ControlStateStatusChangedEvent", b =>
                {
                    b.HasBaseType("RSBController.Application.Domain.Entities.FarmEvent.FarmEvent");

                    b.Property<int>("Initiator")
                        .HasColumnType("integer");

                    b.Property<string>("Reason")
                        .HasColumnType("text");

                    b.Property<int>("Status")
                        .HasColumnType("integer");

                    b.ToTable("ControlStateStatusChangedEvent");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Account", b =>
                {
                    b.OwnsOne("RSBController.Application.Domain.Entities.ScriptAssigment", "ScriptAssigment", b1 =>
                        {
                            b1.Property<long>("AccountId")
                                .HasColumnType("bigint");

                            b1.Property<DateTimeOffset>("At")
                                .HasColumnType("timestamp with time zone");

                            b1.Property<long>("ScriptId")
                                .HasColumnType("bigint");

                            b1.HasKey("AccountId");

                            b1.ToTable("Accounts");

                            b1.WithOwner()
                                .HasForeignKey("AccountId");
                        });

                    b.Navigation("ScriptAssigment");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.ActiveMule", b =>
                {
                    b.HasOne("RSBController.Application.Domain.Entities.Client", null)
                        .WithOne()
                        .HasForeignKey("RSBController.Application.Domain.Entities.ActiveMule", "ClientId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.OwnsOne("RSBController.Application.Domain.ValueObjects.Position", "Position", b1 =>
                        {
                            b1.Property<long>("ActiveMuleId")
                                .HasColumnType("bigint");

                            b1.Property<int>("X")
                                .HasColumnType("integer");

                            b1.Property<int>("Y")
                                .HasColumnType("integer");

                            b1.Property<int>("Z")
                                .HasColumnType("integer");

                            b1.HasKey("ActiveMuleId");

                            b1.ToTable("Mules");

                            b1.WithOwner()
                                .HasForeignKey("ActiveMuleId");
                        });

                    b.Navigation("Position")
                        .IsRequired();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Client", b =>
                {
                    b.OwnsOne("RSBController.Application.Domain.ValueObjects.ActiveSession", "ActiveSession", b1 =>
                        {
                            b1.Property<long>("ClientId")
                                .HasColumnType("bigint");

                            b1.Property<long>("AccountId")
                                .HasColumnType("bigint");

                            b1.Property<long>("ScriptId")
                                .HasColumnType("bigint");

                            b1.Property<DateTimeOffset>("StartedAt")
                                .HasColumnType("timestamp with time zone");

                            b1.HasKey("ClientId");

                            b1.ToTable("Clients");

                            b1.WithOwner()
                                .HasForeignKey("ClientId");
                        });

                    b.Navigation("ActiveSession")
                        .IsRequired();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.MulingRequest", b =>
                {
                    b.HasOne("RSBController.Application.Domain.Entities.ActiveMule", null)
                        .WithMany("MulingRequests")
                        .HasForeignKey("ActiveMuleId");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.PlayerDetails", b =>
                {
                    b.HasOne("RSBController.Application.Domain.Entities.Account", null)
                        .WithOne()
                        .HasForeignKey("RSBController.Application.Domain.Entities.PlayerDetails", "AccountId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.OwnsMany("RSBController.Application.Domain.Entities.SkillExperience", "_skills", b1 =>
                        {
                            b1.Property<int>("Skill")
                                .HasColumnType("integer");

                            b1.Property<long>("PlayerDetailsId")
                                .HasColumnType("bigint");

                            b1.HasKey("Skill", "PlayerDetailsId");

                            b1.HasIndex("PlayerDetailsId");

                            b1.ToTable("SkillExperience");

                            b1.WithOwner()
                                .HasForeignKey("PlayerDetailsId");
                        });

                    b.Navigation("_skills");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.Schedule", b =>
                {
                    b.OwnsMany("RSBController.Application.Domain.ValueObjects.TimeRange", "TimeRanges", b1 =>
                        {
                            b1.Property<long>("ScheduleId")
                                .HasColumnType("bigint");

                            b1.Property<int>("Id")
                                .ValueGeneratedOnAdd()
                                .HasColumnType("integer");

                            NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b1.Property<int>("Id"));

                            b1.HasKey("ScheduleId", "Id");

                            b1.ToTable("TimeRange");

                            b1.WithOwner()
                                .HasForeignKey("ScheduleId");
                        });

                    b.Navigation("TimeRanges");
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.AccountStatusChangedEvent", b =>
                {
                    b.HasOne("RSBController.Application.Domain.Entities.Account", null)
                        .WithMany()
                        .HasForeignKey("AccountId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.FarmEvent.ClientStatusChangedEvent", b =>
                {
                    b.HasOne("RSBController.Application.Domain.Entities.Client", null)
                        .WithMany()
                        .HasForeignKey("ClientId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("RSBController.Application.Domain.Entities.ActiveMule", b =>
                {
                    b.Navigation("MulingRequests");
                });
#pragma warning restore 612, 618
        }
    }
}
