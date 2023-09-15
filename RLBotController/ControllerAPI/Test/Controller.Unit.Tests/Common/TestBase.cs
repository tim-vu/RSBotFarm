using System;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using RSBController.Application.Infrastructure.Persistence;

namespace RSBController.Unit.Tests.Common;

public abstract class TestBase
{
    private readonly DbContextOptions<RSBControllerContext> _options;

    protected TestBase()
    {
        _options = new DbContextOptionsBuilder<RSBControllerContext>()
            .UseInMemoryDatabase(Guid.NewGuid().ToString())
            .Options;
    }

    public RSBControllerContext CreateContext()
    {
        return new RSBControllerContext(_options, null);
    }
}