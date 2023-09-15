// using System;
// using System.Collections.Generic;
// using System.Linq;
// using System.Threading.Tasks;
// using FluentAssertions;
// using Moq;
// using RSBController.Application.Domain.Entities;
// using RSBController.Application.Features.Allocations.Accounts;
// using RSBController.Application.Features.Allocations.Clients;
// using RSBController.Application.Tests.Common;
// using Xunit;
//
// namespace RSBController.Application.Unit.Tests.Allocations.Clients;
//
// public class ClientAllocatorTests : TestBase
// {
//     private readonly Mock<IAccountAllocator> _accountAllocator;
//
//     public ClientAllocatorTests()
//     {
//         _accountAllocator = new Mock<IAccountAllocator>();
//     }
//     
//     [Fact]
//     public async Task CreateAllocations_Should_Return_Correct_Results()
//     {
//         var clients = new List<Client>
//         {
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//         };
//
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//             new("user2", "pass2", null),
//             new("user3", "pass3", null),
//             new("user4", "pass4", null),
//             new("user5", "pass5", null)
//         };
//
//         var scripts = new List<Script>
//         {
//             new("scrip1", 3, 0),
//             new("scrip2", 2, 0),
//         };
//
//         await using (var context = CreateContext())
//         {
//             context.Accounts.AddRange(accounts);
//             context.Scripts.AddRange(scripts);
//             context.Clients.AddRange(clients);
//             await context.SaveChangesAsync();
//         }
//
//         var accountAllocations = new List<AccountAllocationDto>
//         {
//             new()
//             {
//                 Account = accounts[0],
//                 Script = scripts[0]
//             },
//             new()
//             {
//                 Account = accounts[1],
//                 Script = scripts[0]
//             },
//             new()
//             {
//                 Account = accounts[2],
//                 Script = scripts[0]
//             },
//             new()
//             {
//                 Account = accounts[3],
//                 Script = scripts[1]
//             },
//             new()
//             {
//                 Account = accounts[4],
//                 Script = scripts[1]
//             }
//         };
//
//         _accountAllocator.Setup(a => a.AllocateAccounts(TODO, TODO))
//             .ReturnsAsync(accountAllocations);
//
//         await using var context2 = CreateContext();
//         var allocator = new ClientAllocator(_accountAllocator.Object);
//
//         var result = await allocator.AllocateClients(TODO, TODO);
//
//         result.Should().HaveCount(5, "there are 5 AccountAllocations");
//         result.Should().OnlyHaveUniqueItems(e => e.Client.Id, "a client should not be allocated more than once");
//         result.Should().OnlyHaveUniqueItems(e => new AccountAllocationDto
//         {
//             Account = e.Account,
//             Script = e.Script
//         }, "an AccountAllocation cannot be allocated to twice");
//         result.Select(e => new AccountAllocationDto
//         {
//             Account = e.Account,
//             Script = e.Script
//         }).Should().BeEquivalentTo(accountAllocations, "each AccountAllocation should be used");
//     }
//     
//     
//     [Fact]
//     public async Task CreateAllocations_Should_Keep_Existing_Allocations()
//     {
//         var clients = new List<Client>
//         {
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//             new(Guid.NewGuid().ToString()),
//         };
//
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//             new("user2", "pass2", null),
//         };
//
//         var scripts = new List<Script>
//         {
//             new("scrip1", 1, 0),
//             new("scrip2", 1, 0),
//         };
//
//         await using (var context = CreateContext())
//         {
//             context.Accounts.AddRange(accounts);
//             context.Scripts.AddRange(scripts);
//             context.Clients.AddRange(clients);
//             await context.SaveChangesAsync();
//             
//             clients[1].Allocate(scripts[0].Id, accounts[0].Id, DateTime.UtcNow);
//             await context.SaveChangesAsync();
//         }
//
//         var accountAllocations = new List<AccountAllocationDto>
//         {
//             new()
//             {
//                 Account = accounts[0],
//                 Script = scripts[0]
//             },
//             new()
//             {
//                 Account = accounts[1],
//                 Script = scripts[1]
//             }
//         };
//
//         _accountAllocator.Setup(a => a.AllocateAccounts(TODO, TODO))
//             .ReturnsAsync(accountAllocations);
//
//         await using var context2 = CreateContext();
//         var allocator = new ClientAllocator(_accountAllocator.Object);
//
//         var result = await allocator.AllocateClients(TODO, TODO);
//
//         result.Should().HaveCount(2, "there are 2 AccountAllocations");
//         result.Should().OnlyHaveUniqueItems(e => e.Client.Id, "a client should not be allocated more than once");
//         result.Should().OnlyHaveUniqueItems(e => new AccountAllocationDto
//         {
//             Account = e.Account,
//             Script = e.Script
//         }, "an AccountAllocation cannot be allocated to twice");
//         result.Select(e => new AccountAllocationDto
//         {
//             Account = e.Account,
//             Script = e.Script
//         }).Should().BeEquivalentTo(accountAllocations, "each AccountAllocation should be used");
//
//         var client1Allocation = result.FirstOrDefault(c => c.Client.Id == clients[1].Id);
//         client1Allocation.Should().NotBeNull("client1 should be allocated");
//         client1Allocation!.Account.Should().BeEquivalentTo(accounts[0], "this account was already allocated to this client");
//         client1Allocation!.Script.Should().BeEquivalentTo(scripts[0], "this script was already allocated to this client");
//     }
// }