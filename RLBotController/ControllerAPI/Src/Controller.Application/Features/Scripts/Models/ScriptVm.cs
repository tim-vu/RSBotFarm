using System.Collections.Generic;
using Controller.Application.Domain.ValueObjects;

namespace Controller.Application.Features.Scripts.Models;

public sealed record ScriptVm(long Id, string Name, int Instances, int Priority);