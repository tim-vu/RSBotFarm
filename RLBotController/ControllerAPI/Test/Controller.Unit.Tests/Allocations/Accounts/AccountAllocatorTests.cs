// using System;
// using System.Collections.Generic;
// using System.Linq;
// using System.Threading.Tasks;
// using FluentAssertions;
// using RSBController.Application.Domain.Entities;
// using RSBController.Application.Features.Allocations.Accounts;
// using RSBController.Application.Tests.Common;
// using Xunit;
//
// namespace RSBController.Application.Unit.Tests.Allocations.Accounts;
//
// public class AccountAllocatorTests : TestBase
// {
//     [Fact]
//     public async Task CreateAllocations_Should_Return_CorrectAllocations()
//     {
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//             new("user2", "pass2", null),
//             new("user3", "pass3", null),
//             new("user4", "pass4", null),
//             new("user5", "pass5", null),
//         };
//
//         var scripts = new List<Script>
//         {
//             new Script("script1", 3, 0),
//             new Script("script2", 2, 1)
//         };
//         
//         await using (var context1 = CreateContext())
//         {
//             context1.Accounts.AddRange(accounts);
//             context1.Scripts.AddRange(scripts);
//             await context1.SaveChangesAsync();
//         }
//
//         await using var context2 = CreateContext();
//
//         var allocator = new AccountAllocator(context2);
//
//         var result = await allocator.AllocateAccounts(TODO, TODO);
//
//         result.Should().NotBeNull();
//         result.Should().HaveCount(5, "5 accounts are available for 5 total instances");
//         result.Should().OnlyHaveUniqueItems(a => a.Account.Id, "accounts should not be allocated twice");
//
//         result.Where(r => r.Script.Id == scripts[0].Id)
//             .Should()
//             .HaveCount(scripts[0].Instances,
//                 $"{scripts[0].Name} has {scripts[0].Instances} {nameof(Script.Instances)}");
//
//         result.Where(r => r.Script.Id == scripts[1].Id)
//             .Should()
//             .HaveCount(scripts[1].Instances,
//                 $"{scripts[1].Name} has {scripts[1].Instances} {nameof(Script.Instances)}");
//     }
//     
//     [Fact]
//     public async Task CreateAllocations_Should_Not_Allocate_Banned_Accounts()
//     {
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//             new("user2", "pass2", null),
//             new("user3", "pass3", null),
//             new("user4", "pass4", null),
//             new("user5", "pass5", null),
//         };
//         
//         accounts[4].BanAccount();
//
//         var scripts = new List<Script>
//         {
//             new Script("script1", 5, 0),
//         };
//         
//         await using (var context1 = CreateContext())
//         {
//             context1.Accounts.AddRange(accounts);
//             context1.Scripts.AddRange(scripts);
//             await context1.SaveChangesAsync();
//         }
//
//         await using var context2 = CreateContext();
//
//         var allocator = new AccountAllocator(context2);
//
//         var result = await allocator.AllocateAccounts(TODO, TODO);
//
//         result.Should().NotBeNull();
//         result.Should().HaveCount(4, "4 non-banned accounts are available for 5 total instances");
//         result.Should().OnlyHaveUniqueItems(a => a.Account.Id, "accounts should not be allocated twice");
//
//         result.Where(r => r.Script.Id == scripts[0].Id)
//             .Should()
//             .HaveCount(4,
//                 $"4 non-banned accounts are available for {scripts[0].Instances} instances");
//     }
//     
//     [Fact]
//     public async Task CreateAllocations_Should_Reallocate_To_Same_Script()
//     {
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//         };
//
//         var scripts = new List<Script>
//         {
//             new Script("script1", 1, 0),
//             new Script("script1", 2, 1)
//         };
//         
//         await using (var context1 = CreateContext())
//         {
//             context1.Accounts.AddRange(accounts);
//             context1.Scripts.AddRange(scripts);
//             await context1.SaveChangesAsync();
//
//             accounts[0].Allocate(scripts[1].Id, DateTime.UtcNow);
//             await context1.SaveChangesAsync();
//         }
//
//         await using var context2 = CreateContext();
//
//         var allocator = new AccountAllocator(context2);
//
//         var result = await allocator.AllocateAccounts(TODO, TODO);
//
//         result.Should().NotBeNull();
//         result.Should().HaveCount(1, "only 1 account is available for 3 instances");
//
//         result.Where(r => r.Script.Id == scripts[0].Id)
//             .Should()
//             .HaveCount(0, "no account without an existing allocation exists");
//
//         result.Where(r => r.Script.Id == scripts[1].Id)
//             .Should()
//             .HaveCount(1, "the only existing account could not be allocated to a script with a higher priority because of an existing allocation");
//     }
//
//     [Fact]
//     public async Task CreateAllocations_Should_Respect_Priority()
//     {
//         var accounts = new List<Account>
//         {
//             new("user1", "pass1", null),
//             new("user2", "pass2", null),
//         };
//
//         var scripts = new List<Script>
//         {
//             new Script("script1", 2, 0),
//             new Script("script1", 1, 1)
//         };
//         
//         await using (var context1 = CreateContext())
//         {
//             context1.Accounts.AddRange(accounts);
//             context1.Scripts.AddRange(scripts);
//             await context1.SaveChangesAsync();
//         }
//
//         await using var context2 = CreateContext();
//
//         var allocator = new AccountAllocator(context2);
//
//         var result = await allocator.AllocateAccounts(TODO, TODO);
//
//         result.Should().NotBeNull();
//         result.Should().HaveCount(2, "only 2 account are available for 3 instances");
//
//         result.Where(r => r.Script.Id == scripts[0].Id)
//             .Should()
//             .HaveCount(2, "2 accounts exist for 2 instances");
//
//         result.Where(r => r.Script.Id == scripts[1].Id)
//             .Should()
//             .HaveCount(0, "the only existing account has been allocated to a script with a higher priority");
//     }
// }