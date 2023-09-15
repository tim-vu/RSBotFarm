using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.ValueObjects;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Accounts.Workers;

public class UpdateWorkerSchedule
{
    public sealed class Command : ICommand
    {
        public int WorkerId { get; set; }

        public List<TimeRangeDto> TimeRanges { get; set; } = null!;
    }

    public sealed record TimeRangeDto(TimeOnly Start, TimeOnly Stop, long ScriptId);
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var pair = await _context.WorkerAccounts
                .GroupJoin(
                    _context.Schedules,
                    w => w.ScheduleId,
                    s => s.Id,
                    (w, s) => new { Worker = w, Schedule = s.First() }
                )
                .FirstOrDefaultAsync(p => p.Worker.Id == request.WorkerId, cancellationToken);

            if (pair == null)
            {
                throw new NotFoundException($"worker with id {request.WorkerId} not found");
            }

            var worker = pair.Worker;
            var schedule = pair.Schedule;
            
            schedule.UpdateTimeRanges(request.TimeRanges.Select(r => new TimeRange(r.Start, r.Stop, r.ScriptId)).ToList()); 
            
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}